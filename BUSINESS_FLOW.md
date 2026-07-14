# Business Process Flow — Operation Service (SmartDMS)
លម្អិតពេញលេញនៃដំណើរការអាជីវកម្មនីមួយៗ (Full detail of every business process)

---

## 1. Transfer Order — Warehouse → Sub-Distributor

**Entities:** `TransferOrder`, `TransferOrderItem`, `WarehouseStock`, `Stock`, `StockLedger`
**Base path:** `/api/transfer-orders`

### State machine
```
REQUEST --approve--> APPROVED --ship--> SHIPPED --receive--> RECEIVED
   |                     |
   +--------cancel-------+
```

### Step-by-step

| # | Endpoint | Guard (must be) | What happens internally |
|---|---|---|---|
| 1 | `POST /api/transfer-orders` | — | Creates `TransferOrder` with `status=REQUEST`, `createdAt/updatedAt=now`. Builds `TransferOrderItem` rows from `items[]` (`productId`, `qty`), wires `item.transferOrder` back-reference, cascades save. |
| 2 | `PUT /{id}/approve` | status == `REQUEST` | `status → APPROVED`, `updatedAt=now`. No stock movement yet. |
| 3 | `PUT /{id}/ship` | status == `APPROVED` | For each item: **recordOutbound** (below). `startedAt=now`, `status → SHIPPED`. |
| 4 | `PUT /{id}/receive` | status == `SHIPPED` | For each item: **recordInbound** (below). `completedAt=now`, `status → RECEIVED`. |
| 5 | `PUT /{id}/cancel` | status ∈ {`REQUEST`,`APPROVED`} | `status → CANCELLED`. No stock reversal needed since nothing moved yet. |
| — | `PUT /{id}` | — | Edits header fields only (`transferNo`, `fromWarehouseId`, `toSdId`, `transferDate`; `driverId`/`vehicleId`/`remark` only overwritten if provided) — never touches `status` or `items`. |

Any guard violation → `InvalidStateException` → **409 Conflict** (e.g. *"Expected status APPROVED but was REQUEST"*).

### recordOutbound(order, item) — runs on `ship`
1. Look up `WarehouseStock` by `(fromWarehouseId, productId)`. Not found → **409** (*"No stock for product X in warehouse Y"*).
2. Check `qtyOnHand >= qty`. Not enough → **409** (*"Not enough stock... Have X, need Y"*).
3. `WarehouseStock.qtyOnHand -= qty`, save.
4. Insert `StockLedger` row: `trxType=OUT`, `qtyOut=qty`, `warehouseId=fromWarehouseId`, `balanceQty = previousBalance - qty` (previous balance = latest ledger row for that product+warehouse, or 0).

### recordInbound(order, item) — runs on `receive`
1. Look up `Stock` by `(toSdId, productId)`. Not found → **create new** `Stock` with `qtyOnHand=0` (first delivery to that SD for that product).
2. `Stock.qtyOnHand += qty`, save.
3. Insert `StockLedger` row: `trxType=IN`, `qtyIn=qty`, `sdId=toSdId`, `balanceQty = previousBalance + qty`.

### Request/Response shapes
```json
// POST /api/transfer-orders
{
  "transferNo": "TO-001",
  "fromWarehouseId": 1,
  "toSdId": 1,
  "driverId": 1,
  "vehicleId": 1,
  "transferDate": "2026-07-12T09:00:00",
  "remark": "...",
  "items": [{ "productId": 1, "qty": 20 }]
}
```
Response adds: `id`, `status`, `startedAt`, `completedAt`, `createdAt`, `updatedAt`, and each item enriched with `productName`/`barcode` (looked up server-side from `ProductRepository` — client never sends those).

---

## 2. Order-to-Cash — Sales Order → Invoice → Collection

**Entities:** `SalesOrder`, `SalesOrderItem`, `Invoice`, `Collection`
**Base paths:** `/api/sales-orders`, `/api/invoices`, `/api/collections`

### Flow
```
SalesOrder (items + total, status="Pending")
        │
        ▼
   Invoice (one per SalesOrder — enforced)
   status = UNPAID / PARTIAL / PAID  (derived from amounts)
        │
        ▼
   Collection (one payment record per call)
   → adds to Invoice.paidAmount → Invoice.status recalculated
```

### 2.1 Sales Order
`POST /api/sales-orders`:
```json
{
  "customerId": 1, "salesmanId": 1, "sdId": 1,
  "orderDate": "2026-07-12T09:00:00", "note": "...",
  "items": [{ "productId": 1, "qty": 5, "unitPrice": 0.50 }]
}
```
- `orderNo` auto-generated: `"SO-" + System.currentTimeMillis()` (client never sends it).
- `status` hardcoded to `"Pending"` on create (no workflow endpoints to advance it in this service — status changes for a sales order aren't currently exposed via REST).
- Each item's `lineTotal = unitPrice × qty`; `totalAmount` = sum of all line totals.
- Response resolves `customerName`, `salesmanName`, and `sdName` (looked up via `UserRepository` — note: in this table, "SD" for a sales order is modeled as a `User`, not a `SubDistributor` — a naming overlap worth knowing about, not a bug).
- `PUT /{id}` only edits header fields (`customerId`, `salesmanId`, `sdId`, `orderDate`, `note`) — items are not editable after creation.

### 2.2 Invoice
`POST /api/invoices`:
```json
{ "invoiceNo": "INV-001", "salesOrderId": 1, "customerId": 1,
  "totalAmount": 2.50, "paidAmount": 0 }
```
- Guard 1: `invoiceNo` must be unique → **409** if duplicate (check only runs when `invoiceNo` is provided — a null/omitted one skips it).
- Guard 2: **one invoice per `salesOrderId`** → **409** if that sales order already has an invoice (same null-skips-the-check behavior). Now also enforced on `update()`, not just `create()`.
- `status` auto-derived by `resolveStatus(total, paid)`:
  - `paid <= 0` → `UNPAID`
  - `paid >= total` → `PAID`
  - else → `PARTIAL`
  - (caller can force a status explicitly via `request.status` if needed)
- Response includes computed `balanceDue = totalAmount - paidAmount`.

### 2.3 Collection (payment against an invoice)
`POST /api/collections`:
```json
{ "invoiceId": 1, "customerId": 1, "amount": 1.00, "paymentMethod": "CASH", "collectedBy": 3 }
```
1. `amount` must be `> 0` → else **400** (`IllegalArgumentException`).
2. `amount` must not exceed the invoice's remaining balance (`total - alreadyPaid`) → else **400** (*"Collection (X) exceeds remaining balance (Y)"*).
3. Saves the `Collection` row (`collectionDate=now`).
4. Updates the parent `Invoice.paidAmount += amount`, recalculates `status`, saves.
- `DELETE /{id}` reverses the effect: subtracts the collection's amount back off the invoice's `paidAmount` (floored at 0) and recalculates status, **then** deletes the collection row.

---

## 3. Delivery — Fulfillment

**Entity:** `Delivery`
**Base path:** `/api/deliveries`

### State machine
```
PENDING → STARTED → ARRIVED → COMPLETED
                            ↘ FAILED
```
(`PENDING`/`FAILED` carry no timestamp; `STARTED`→`startedAt`, `ARRIVED`→`arrivedAt`, `COMPLETED`→`completedAt` are stamped automatically.)

### Endpoints
| Endpoint | Purpose |
|---|---|
| `POST /api/deliveries` | Create. `deliveryNo` auto-generated (`"DLV-"+timestamp`) if not supplied; must be unique → **409** if collided (checked via `existsByDeliveryNo`). `status="PENDING"` on create. |
| `PUT /{id}` | Edit header fields only (invoice/customer/driver/vehicle/routePlan/driverAssignment/date/remark) — status untouched here. |
| `PATCH /{id}/status` `{ "status": "COMPLETED" }` | Advances status and stamps the matching timestamp per the switch above. |
| `GET /api/deliveries?driverId=8&date=2026-07-01` | List a driver's deliveries for a given day (defaults to today if `date` omitted). |
| `DELETE /{id}` | Hard delete. |

Response resolves and embeds: driver `fullName`, `customerName`, `invoiceNo`, `vehicleNo` — all looked up server-side from their respective repositories so the client never has to join data itself.

---

## 4. Route Plan → Driver Assignment → Driver Mobile App

**Entities:** `RoutePlan`, `RoutePlanDetail`, `DriverAssignment`
**Base paths:** `/api/route-plans`, `/api/driver-assignments`, `/api/driver`

### 4.1 Route Plan
`POST /api/route-plans`:
```json
{ "salesmanId": 1, "description": "Monday route", "isActive": true,
  "details": [{ "customerId": 1, "dayOfWeek": "MONDAY", "sequenceNo": 1 }] }
```
- **One active route plan per salesman** — `existsBySalesmanId` guard on create, and on update too (excluding itself) → **409** if violated.
- `PUT /{id}` **replaces** all `RoutePlanDetail` rows (`orphanRemoval` deletes the old set, then re-adds from the request) rather than patching individual lines.
- `DELETE /{id}` is a **soft delete** (`isActive=false`), not a hard delete.

### 4.2 Driver Assignment
`POST /api/driver-assignments`: links a `driverId` (a `User`) + `routePlanId` + `vehicleNo` + `assignedDate` + `status`. No uniqueness constraint currently enforced (a driver could theoretically get two overlapping assignments — no guard exists here).

⚠️ **Known gap:** the request DTO only accepts `vehicleNo` (a free-text string), never `vehicleId`. Nothing in `DriverAssignmentServiceImpl` ever sets the entity's `vehicleId` field. See 4.3 — `vehicle-info` depends on `vehicleId`, so it currently can't resolve a plate/model from data created through this endpoint.

### 4.3 Driver mobile endpoints (read-only, aggregated views)
All three derive their data from `TransferOrder` rows matching `driverId` + `transferDate` within the given day (`findByDriverIdAndTransferDateBetweenOrderByIdAsc`, `00:00:00`–`23:59:59`):

| Endpoint | Returns |
|---|---|
| `GET /api/driver/stops?driverId=&date=` | One `DriverStopResponse` per transfer order for that day: stop number, destination (SD → resolved via `Customer` by `toSdId`), lat/lng, item/qty totals, and a **UI-friendly status label** mapped from the raw status: `RECEIVED→Completed`, `SHIPPED→In Progress`, `CANCELLED→Cancelled`, else `Pending`. |
| `GET /api/driver/summary?driverId=&date=` | Aggregate counts: `totalStops`, `completed`, `inProgress`, `pending`, and `progressPercent` (`completed/total × 100`, rounded). |
| `GET /api/driver/vehicle-info?driverId=` | Resolves the driver's **most recent** `DriverAssignment` (highest id = newest), then the linked `Vehicle` (plate/model, by `vehicleId`) and `RoutePlan` (name, falling back to `"ROUTE-"+id` if no description). Returns an empty response if the driver has no assignment at all — and, per the gap noted in 4.2, `licensePlate`/`vehicleModel` will also come back `null` even with an assignment, since nothing currently populates `vehicleId` on create. |

---

## 5. Customer Checkin — Salesman Visit Tracking

**Entity:** `CustomerCheckin`
**Base path:** `/api/customer-checkins`

Simple visit log: `POST` requires an existing `salesmanId` (`User`) and `customerId` (`Customer`) — 404 if either doesn't exist — then stores `latitude`/`longitude`/`checkinTime=now`. No status/workflow; it's a GPS-stamped visit record used for sales-rep field tracking. Response always resolves `salesmanName` and `customerName`.

---

## 6. Sales KPI — Planned vs Actual Visit Performance

**Entities used:** `RoutePlan` + `RoutePlanDetail` (the plan) × `CustomerCheckin` (the actual proof-of-visit)
**Base path:** `/api/sales-kpi`
**New in this session** — ties two existing features together into a performance metric; no new table was needed.

### The idea
```
RoutePlan (salesman's weekly plan: which customer, which day-of-week, what order)
        │
        ▼
   Every day, the salesman is expected to visit that day's planned customers
        │
        ▼
   CustomerCheckin (salesman actually checks in at the customer, GPS-stamped)
        │
        ▼
   KPI = did the check-ins match the plan?  (completion rate)
```

`RoutePlanDetail.dayOfWeek` (e.g. `"MONDAY"`) is compared against each calendar date's actual day-of-week for every date in the requested range. For each planned stop, the service looks for a `CustomerCheckin` from that same salesman, for that same customer, on that same calendar date. Found → **visited**; not found → **missed**.

### Endpoint
`GET /api/sales-kpi?salesmanId=1` → today only.
`GET /api/sales-kpi?salesmanId=1&startDate=2026-07-06&endDate=2026-07-12` → a full week.

```json
{
  "salesmanId": 1,
  "salesmanName": "Sok Dara",
  "startDate": "2026-07-06",
  "endDate": "2026-07-12",
  "plannedVisits": 12,
  "completedVisits": 9,
  "completionRate": 75.0,
  "extraVisits": 2,
  "days": [
    {
      "date": "2026-07-06",
      "dayOfWeek": "MONDAY",
      "plannedCount": 3,
      "visitedCount": 2,
      "completionRate": 66.7,
      "stops": [
        { "customerId": 1, "customerName": "KH Store Mart", "sequenceNo": 1, "visited": true,  "checkinTime": "2026-07-06T08:41:00" },
        { "customerId": 2, "customerName": "ABC Shop",      "sequenceNo": 2, "visited": false, "checkinTime": null }
      ]
    }
  ]
}
```

- `plannedVisits` / `completedVisits` / `completionRate` — totals across the whole date range. This is the headline KPI number: **did the salesman actually go where the plan said, or not**.
- `extraVisits` — check-ins that happened but weren't on that day's plan (walk-ins / unplanned visits) — counted separately so they don't distort the compliance rate either way.
- `days[]` — day-by-day breakdown, so a manager can see exactly which stop was missed on which day, not just one aggregate number.
- No `RoutePlan` at all for that salesman → `plannedVisits=0` and `completionRate=0` for the whole range (nothing to comply with — not treated as a free 100%).
- **404** if `salesmanId` doesn't resolve to a `User`; **400** if `endDate` is before `startDate`.

### Why this wasn't a new table
`RoutePlan`/`RoutePlanDetail` already records **intent**, and `CustomerCheckin` already records **fact** (with a real GPS timestamp, so it can't be backfilled after the fact). The KPI is a **derived/computed view** joining the two by `(customerId, calendar date)` — not new state to persist. A checkin stays just a checkin; the "was this on-plan" judgment is computed fresh on every request, so the source of truth never drifts into two places.

---

## 7. Stock — three tables, three purposes

| Table | Represents | Key | Updated by |
|---|---|---|---|
| `WarehouseStock` | Current balance **at a warehouse** | unique `(warehouse_id, product_id)` | Manual CRUD (`/api/warehouse-stocks`) **and** automatically decremented on Transfer Order `ship` |
| `Stock` | Current balance **at a Sub-Distributor** | `(sd_id, product_id)` (app-enforced, guarded in both `create`/`update` as of this session) | Manual CRUD (`/api/stocks`) **and** automatically incremented on Transfer Order `receive` |
| `StockLedger` | **Immutable history** of every IN/OUT movement | none (append-only) | Written automatically by Transfer Order `ship`/`receive`; also has its own manual CRUD (`/api/stock-ledgers`) for corrections |

Think of `WarehouseStock`/`Stock` as **current balance** (like a bank account balance) and `StockLedger` as the **transaction history** (like bank statement lines) — the same relationship as double-entry accounting.

---

## 8. Cross-cutting: how every flow reports errors

Every guard described above throws one of five exception types, all normalized by `GlobalExceptionHandler` into the same JSON shape (`timestamp`, `status`, `error`, `message`):

| Exception | HTTP status | Used for |
|---|---|---|
| `ResourceNotFoundException` | 404 | Referenced id doesn't exist (product, customer, warehouse, order, ...) |
| `ResourceAlreadyExistsException` | 409 | Duplicate unique field (barcode, vehicleNo, sdCode, deliveryNo, one-per-salesman route plan, one-per-order invoice, one-per-warehouse+product stock row...) |
| `InvalidStateException` | 409 | Illegal state transition (ship before approve, cancel after shipped, insufficient stock) |
| `IllegalArgumentException` / `IllegalStateException` | 400 | Input-level validation (e.g. collection amount ≤ 0 or over balance) |
| `DataIntegrityViolationException` (safety net) | 409 | Any DB constraint violation not caught by an explicit guard above |
