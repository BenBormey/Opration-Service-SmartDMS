# Demo Script — Operation Service (SmartDMS)
សម្រាប់បង្ហាញគ្រូ (Presentation script for the teacher demo)

---

## 1. សេចក្តីណែនាំគម្រោង (Introduction — ~1 min)

និយាយថា:

> "ថ្ងៃនេះខ្ញុំសូមធ្វើបទបង្ហាញ **Operation Service** ដែលជាផ្នែកមួយនៃប្រព័ន្ធ **SmartDMS (Smart Distribution Management System)**។ សេវានេះសរសេរដោយ **Java 21 + Spring Boot 3.5**, ប្រើ **PostgreSQL** ជា database, **JWT** សម្រាប់ security, និង **Swagger/OpenAPI** សម្រាប់ document API។"

Key facts (mention if asked):
- `com.smartdms:operation-service`, Java 21, Spring Boot 3.5.15
- Layers: `controller` → `service` → `repository` → PostgreSQL (schema `dms`)
- Auth: stateless JWT, token issued by a separate **auth-service** (same secret, shared DB)
- 20 resources: Product, Category, Supplier, Customer, SalesOrder, Delivery, TransferOrder, Stock, Vehicle, Driver, RoutePlan, Invoice, Collection, ...

---

## 2. ទីតាំង Operation Service ក្នុងប្រព័ន្ធទាំងមូល (Where this fits in the full system — ~2 min)

> ប្រើ slide **System Architecture** (SETEC Institute) ជាឆាកបើក មុននឹង zoom ចូល operation-service។

និយាយថា:

> "មុននឹងចូល code, ខ្ញុំសូមបង្ហាញថា operation-service ដែលខ្ញុំកំពុងបង្ហាញ ស្ថិតនៅត្រង់ណា ក្នុងស្ថាបត្យកម្មទាំងមូលរបស់ប្រព័ន្ធ។"

Draw / point at the diagram, layer by layer:

```
Client (Web / Desktop / Mobile App)
        ↕
   API Gateway  (open-source or custom — routes requests to the right microservice)
        ↕
┌─────────────────────────── Microservices (Back-End) ───────────────────────────┐
│  Service_1: Auth (User & Permission)                                           │
│  Service_2: Reporting                                                          │
│  Service_3: Operation   ← នេះជាអ្វីដែលខ្ញុំកំពុងបង្ហាញថ្ងៃនេះ                     │
└──────────────────────────────────────────────────────────────────────────────┘
        ↕                                          ↕
┌─────────── Middleware ───────────┐                │
│  Service_4: CRON Service          │                │
└──────────────────────────────────┘                │
        ↕                                            ↓
                                              (DB) PostgreSQL
```

**ការតភ្ជាប់ជាក់ស្តែងរវាង operation-service និង microservice ផ្សេងទៀត (how it actually integrates, not just theory):**

| ចំណុចភ្ជាប់ (Integration point) | ស្ថានភាពក្នុង operation-service |
|---|---|
| Auth (Service_1) | operation-service មិន issue JWT ដោយខ្លួនឯងទេ — វា **verify** token ដែល Service_1 ចេញឲ្យ, ដោយប្រើ **secret ដូចគ្នា** (`app.jwt.secret`) និង **database schema ដូចគ្នា** (`smartdm` / schema `dms`) |
| API Gateway | operation-service បើក Swagger/OpenAPI docs (`/v3/api-docs`) ដែល Gateway ឬ front-end អាចអាន schema បាន; CORS ត្រូវបាន config រួចជាស្រេចសម្រាប់ Web/Mobile client |
| Reporting (Service_2) | operation-service ជាប្រភព **stock ledger / sales order / delivery** data ដែល Reporting service អាចអានតាម DB schema `dms` តែមួយ (shared DB, មិនមែន shared code) |
| CRON (Service_4) | មិនទាន់ implement ក្នុង repo នេះទេ (វានឹងជា service ដាច់ដោយឡែក); operation-service រៀបចំរួចជាស្រេចសម្រាប់វា ដោយមាន timestamp fields (`createdAt`/`updatedAt`) និង status fields លើគ្រប់ entity សំខាន់ៗ ដែល CRON job អាចអានបាន (ឧ. auto-cancel expired REQUEST orders) |

**ការតភ្ជាប់ទៅនឹងលក្ខខណ្ឌ Midterm (mapping to the Midterm rubric on the slide):**

| Midterm requirement | ស្ថានភាព (Status) |
|---|---|
| 1) Complete Design Database | ✅ Schema `dms`, ~20 entities ជាមួយ relations (Product↔Category↔Supplier, TransferOrder↔TransferOrderItem, StockLedger, ...) |
| 2) Complete Microservices | ✅ operation-service (Service_3) complete — layered, DTO-based, JWT-secured, Swagger-documented. *(Service_1/Service_2 status: បញ្ជាក់ដោយសមាជិកក្រុមដែលទទួលខុសត្រូវផ្នែកនោះ)* |
| 3) CRON Service | ⚠️ មិនមែនផ្នែករបស់ operation-service — ជា Service_4 ដាច់ដោយឡែក; operation-service បានផ្តល់ data/status fields ដែល CRON អាចប្រើបាន ប៉ុន្តែ CRON job ខ្លួនឯងមិននៅក្នុង repo នេះទេ |

> "សូមកត់សម្គាល់ថា ថ្ងៃនេះខ្ញុំបង្ហាញលម្អិតតែ **operation-service** ព្រោះនោះជាផ្នែកដែលខ្ញុំទទួលខុសត្រូវ — ប៉ុន្តែវាត្រូវបានរចនាឲ្យសមស្របនឹងប្រព័ន្ធធំទាំងមូលរួចហើយ (shared DB schema, shared JWT secret, Swagger contract សម្រាប់ Gateway)។"

---

## 3. រចនាសម្ព័ន្ធគម្រោង (Architecture of operation-service — ~2 min)

បើក IDE ហើយបង្ហាញ folder structure នេះ:

```
controller/     -> REST endpoints (@RestController), one per resource
service/        -> interfaces (business contract)
service/impl/   -> implementations (business logic + transactions)
dto/<feature>/  -> Request / Response objects (never expose entities over REST)
entity/         -> JPA @Entity classes (map to Postgres tables, schema "dms")
repository/     -> Spring Data JpaRepository interfaces
exception/      -> custom exceptions + @RestControllerAdvice (centralized error handling)
config/         -> Security (JWT filter), OpenAPI/Swagger config
security/       -> AuthUser principal (who's making the request)
```

និយាយថា:

> "គម្រោងនេះប្រើ **layered architecture** ស្តង់ដារនៃ Spring Boot: Controller មិនដែលនិយាយផ្ទាល់ជាមួយ Repository ទេ វាឆ្លងកាត់ Service។ ហើយ Controller មិនដែលបញ្ជូន JPA Entity ចេញទៅ client ដោយផ្ទាល់ទេ — វាតែងតែប្តូរទៅជា **DTO (Request/Response)** សិន ដើម្បីកុំឲ្យ database schema ជាប់ជាមួយ API contract។"

---

## 4. របៀបដំណើរការ (How to run — ~2 min)

```bash
# 1) PostgreSQL ត្រូវតែដំណើរការ, DB name = smartdm, schema = dms
# 2) (បើមាន) start auth-service ជាមុន ដើម្បីយក JWT token
# 3) Run the operation-service
./mvnw spring-boot:run
```

- Server port: **8083**
- Swagger UI: **http://localhost:8083/swagger-ui/index.html**
- Raw OpenAPI spec: **http://localhost:8083/v3/api-docs**

Config values are externalized (env vars with local defaults), so switching DB/secret for a different machine doesn't need a code change:
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `CORS_ALLOWED_ORIGINS`.

---

## 5. ការចូល Authorize ជាមួយ JWT (~1 min)

> "គ្រប់ endpoint ទាំងអស់ (លើកលែងតែ Swagger docs) តម្រូវឲ្យមាន JWT token ត្រឹមត្រូវ។ Token នេះមិនមែនចេញពី operation-service ទេ — វាចេញពី **auth-service (Service_1)** តាមរយៈ login endpoint របស់វា។"

Steps:
1. Login on auth-service → copy the `accessToken`.
2. On Swagger UI, click **Authorize** (top-right, lock icon).
3. Paste the token (no need to type `Bearer `, Swagger adds it).
4. Now every "Try it out" call carries the token automatically.

*(If auth-service isn't running for the demo, have a pre-generated token ready as backup — see §9.)*

---

## 6. ការបង្ហាញផ្ទាល់ (Live Demo — ~8–10 min)

**Pre-demo checklist** — make sure these already exist in the DB (or create them via their own POST endpoints right before the demo), otherwise the Transfer Order response will show `null` names instead of resolved ones:
`Warehouse`, `SubDistributor` (seeded directly in the DB — no REST endpoint for it anymore), `Driver`/`User`, `Vehicle` (`/api/vehicles`), plus the `Category` → `Product` → `Supplier` you'll create live in 6.1/6.2.

### 6.1 Simple CRUD — Category (warm-up, ~1 min)

Show the basic REST pattern first, since it's the simplest resource.

`POST /api/categories`
```json
{
  "categoryName": "Beverages",
  "description": "Drinks and beverages"
}
```
→ `GET /api/categories` to show it back, then `GET /api/categories/{id}`.

Talking point:
> "នេះជា pattern គ្រឹះ: Controller → Service.create(request) → map ទៅ Entity → save → map ត្រឡប់ជា Response DTO។"

### 6.2 Product — relations + validation (~2 min)

`POST /api/products`
```json
{
  "barcode": "8850001234567",
  "productName": "Coca-Cola 330ml",
  "categoryId": 1,
  "supplierId": 1,
  "unit": "Can",
  "buyingPrice": 0.30,
  "sellingPrice": 0.50,
  "wholesalePrice": 0.45,
  "reorderLevel": 100,
  "isActive": true
}
```

- Call it twice with the **same barcode** → second call returns **409 Conflict** (`ResourceAlreadyExistsException`).
- `GET /api/products/9999` (a non-existent id) → **404 Not Found** (`ResourceNotFoundException`).

Talking point:
> "កំហុសទាំងអស់ត្រូវបានចាប់ដោយ **GlobalExceptionHandler** មួយកន្លែង ហើយប្តូរទៅជា JSON response ស្តង់ដារ (timestamp, status, error, message) — មិនមែនលេចធ្លាយ stack trace ចេញទៅ client ទេ។"

### 6.3 Transfer Order — the full business workflow (★ main feature, ~4 min)

> "នេះជា feature ស្មុគស្មាញបំផុត — ការផ្ទេរទំនិញពី Warehouse ទៅ Sub-Distributor (SD), ជាមួយនឹង **state machine**: `REQUEST → APPROVED → SHIPPED → RECEIVED` (ឬ `CANCELLED`)។"

**Step 1 — Create (status becomes REQUEST):**
`POST /api/transfer-orders`
```json
{
  "transferNo": "TO-DEMO-001",
  "fromWarehouseId": 1,
  "toSdId": 1,
  "driverId": 1,
  "vehicleId": 1,
  "transferDate": "2026-07-11T09:00:00",
  "remark": "Demo run for class",
  "items": [
    { "productId": 1, "qty": 20 }
  ]
}
```

**Step 2 — Approve:** `PUT /api/transfer-orders/{id}/approve` → status `APPROVED`.

**Step 3 — Ship:** `PUT /api/transfer-orders/{id}/ship` → status `SHIPPED`.
> "ពេល ship, ប្រព័ន្ធកាត់ស្តុកចេញពី warehouse ដោយស្វ័យប្រវត្តិ ហើយកត់ត្រា **StockLedger** (`trxType = OUT`)។ បើស្តុកមិនគ្រប់ វានឹង **reject ជាមួយ 409**។"

**Step 4 — Receive:** `PUT /api/transfer-orders/{id}/receive` → status `RECEIVED`.
> "ពេល receive, ស្តុកចូល SD ដោយស្វ័យប្រវត្តិ ហើយកត់ត្រា ledger ថ្មី (`trxType = IN`)។"

**Negative-path demo (important!):**
- Create a second order, then try `PUT /{id}/ship` **without approving first** → **409 Conflict**: *"Expected status APPROVED but was REQUEST"*.
- Try `PUT /{id}/cancel` on an order that's already `SHIPPED` → **409 Conflict**: *"Cannot cancel an order in status SHIPPED"*.

Talking point:
> "ច្បាប់ business logic ទាំងនេះ (state machine, stock validation) ស្ថិតនៅក្នុង Service layer តែមួយកន្លែង (`TransferOrderServiceImpl`), មិនមែននៅ Controller ឬ Entity ទេ — ធ្វើឲ្យវាងាយ test និង maintain។ នេះក៏ជាចំណុចដែល **Service_4 (CRON)** អាចភ្ជាប់មកពេលក្រោយ (ឧ. auto-cancel REQUEST ដែលនៅសល់លើសពី 24 ម៉ោង)។"

### 6.4 GET all + filter (~1 min)
`GET /api/transfer-orders` → show the list with nested `items[]`, each item enriched with `productName`/`barcode` (looked up from the Product repository) even though the client only sent `productId`.

---

## 7. ចំណុចសំខាន់ត្រូវសង្កត់ធ្ងន់ (Key points to emphasize)

1. **Layered architecture** — Controller / Service / Repository, each with one job.
2. **DTO pattern everywhere** — no JPA entity ever goes over the wire; request validation and response shape are decoupled from the DB schema.
3. **Centralized exception handling** — one `@RestControllerAdvice`, consistent JSON error format, correct HTTP status codes (404 / 409 / 400).
4. **Stateless JWT security** — shared secret with auth-service, no server-side session, `SessionCreationPolicy.STATELESS`.
5. **Business rules as a state machine** — Transfer Order status transitions are guarded (`InvalidStateException`), so illegal transitions are impossible via the API.
6. **Clean, consistent naming** — every package lowercase, every `ServiceImpl` named consistently, no dead/duplicate code left in the repo (mention this if the teacher checks the codebase directly).
7. **Designed to plug into the full system** — shared DB schema for Reporting, shared JWT secret for Auth, Swagger contract for the Gateway/Front-End, timestamp/status fields ready for the CRON service.

---

## 8. គម្រោងបម្រុង បើ Demo មានបញ្ហា (Backup plan)

- មាន **Postman collection** ឬ **screenshots** នៃការហៅ API ជោគជ័យ ទុកជាបម្រុង។
- មាន **JWT token ដែល generate ជាមុន** ទុកមួយ ក្នុងករណី auth-service មិនដំណើរការ។
- បើ DB មិន connect: បើក `application.properties`, បង្ហាញថា connection string ប្តូរបានតាម environment variable (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) — មិនចាំបាច់ hardcode ក្នុង code។

---

## 9. សំណួរដែលអាចរំពឹងទុក (Anticipated Q&A)

| សំណួរ (Question) | ចម្លើយខ្លី (Short answer) |
|---|---|
| ហេតុអ្វីប្រើ DTO ជំនួស Entity ដោយផ្ទាល់? | ការពារ internal schema leak, គ្រប់គ្រង validation/serialization ដោយឡែក, ជៀសវាង infinite recursion លើ bidirectional JPA relations |
| ហេតុអ្វី JWT stateless? | Scale ងាយ (no server session), ត្រូវនឹង microservice ច្រើនសេវា (auth-service ចេញ token, resource services ត្រឹមតែ verify) |
| Transaction គ្រប់គ្រងយ៉ាងណា? | `@Transactional` នៅ Service layer (មិនមែន Controller/Repository) — status change + stock ledger + stock update ត្រូវ commit ឬ rollback ជាមួយគ្នា |
| បើ approve endpoint ត្រូវហៅដោយ role ណាមួយប៉ុណ្ណោះ? | បច្ចុប្បន្ន security check តែ "មាន token ត្រឹមត្រូវ" (`authenticated()`); role-based restriction អាចបន្ថែមតាម `hasRole(...)` នៅពេលក្រោយ |
| ហេតុអ្វី stock ចេញ/ចូល កត់ត្រាទៅ StockLedger ដាច់ពី Stock/WarehouseStock? | `Stock`/`WarehouseStock` ជា **current balance** (snapshot), `StockLedger` ជា **history/audit trail** នៃរាល់ transaction — ស្រដៀង double-entry accounting |
| ហេតុអ្វី operation-service មិន implement CRON ខ្លួនឯង? | តាម diagram ស្ថាបត្យកម្ម, CRON (Service_4) ជា **Middleware** ដាច់ដោយឡែក មិនមែនផ្នែកនៃ Business microservice ណាមួយទេ — operation-service ត្រឹមតែផ្តល់ data/status fields ដែល CRON job ត្រូវការ |
| Operation Service ដឹងអំពី Auth/Reporting service តាមរបៀបណា? | មិនមានការហៅ code គ្នាទៅវិញទៅមកទេ (no direct service-to-service call ក្នុង repo នេះ) — ការតភ្ជាប់គឺឆ្លងកាត់ **shared DB schema** និង **shared JWT secret** ប៉ុណ្ណោះ, ស្របតាមគោលការណ៍ microservice decoupling |
