# PastillaControl Android App

Aplicacion Android nativa para gestionar medicamentos, horarios y recordatorios de toma.

## Objetivo del proyecto

PastillaControl busca cubrir un flujo base de adherencia terapeutica:

- registrar medicamentos,
- definir horarios de toma,
- disparar recordatorios locales,
- y dejar preparada la base para historial y escalamiento de dosis omitidas.

El proyecto esta pensado para evolucionar de MVP a una app de seguimiento con integracion backend y notificaciones para familia/cuidadores.

## Estado actual (MVP)

Actualmente incluye:

- navegacion principal entre Home, Medications, Schedule y History,
- CRUD basico de medicamentos (alta, edicion, baja por long press),
- alta y baja local de horarios (daily/weekly/interval),
- cliente Retrofit con autenticacion contra backend (`/auth/login`, `/auth/register`),
- fallback local en memoria cuando falla la red o backend,
- receptor y scheduler de alarmas con manejo de `SCHEDULE_EXACT_ALARM` en Android 12+,
- canales de notificacion para reminders y escalations,
- pruebas instrumentadas de flujo principal.

## Stack tecnico

- Lenguaje: Kotlin
- UI: XML + Fragments + ViewBinding
- Arquitectura: capas `ui`, `data`, `domain`, `core`
- Navegacion: Android Navigation Component
- Persistencia: Room (schema modelado) + store en memoria para fallback
- Concurrencia: Kotlin Coroutines
- Jobs en background: WorkManager (scaffold)
- Networking: Retrofit + OkHttp
- Backend auth/session: token Bearer en `SharedPreferences`
- Servicios integrados en dependencias: Firebase Auth, Firestore, FCM (base lista)

## Requisitos

- Android Studio (recomendado: version reciente con AGP 9.x)
- JDK 11
- Android SDK:
  - `minSdk 24`
  - `targetSdk 36`
- Emulador Android o dispositivo fisico

## Configuracion y ejecucion

1. Clonar repositorio:

```bash
git clone https://github.com/lucasbellesi/pastilla-control-android-app.git
cd pastilla-control-android-app
```

2. Abrir el proyecto en Android Studio.

3. Verificar URL del backend en `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "BACKEND_BASE_URL", "\"http://10.0.2.2:8000/api/v1/\"")
```

- `10.0.2.2` funciona para emulador Android conectando a `localhost` de tu maquina.
- Si usas dispositivo fisico, reemplazar por la IP local del host backend.

4. Ejecutar la app (`Run app`) sobre emulador o dispositivo.

## Backend esperado

La app consume endpoints REST versionados en `/api/v1/`.

Endpoints usados actualmente:

- `POST /auth/register`
- `POST /auth/login`
- `GET /medications/`
- `POST /medications/`
- `PUT /medications/{id}`
- `DELETE /medications/{id}`
- `GET /schedules/`
- `POST /schedules/`

Notas:

- Si no existe sesion, se crea/usa una cuenta demo local y se intenta login/registro automatico.
- Ante error de red o backend, las operaciones pasan a fallback local en memoria.

## Permisos Android

Declarados en `AndroidManifest.xml`:

- `INTERNET`
- `POST_NOTIFICATIONS`
- `RECEIVE_BOOT_COMPLETED`
- `SCHEDULE_EXACT_ALARM`

Se incluye receptor para reinicio/cambios de hora y scheduler con fallback a alarmas inexactas cuando no se puede programar exact alarm.

## Estructura del proyecto

```text
app/src/main/java/com/example/pastillacontrol/
  core/
    notifications/   # canales, receivers, scheduler
    work/            # workers (scaffold)
  data/
    local/           # entities, dao, room db, stores
    remote/          # retrofit api, client, session store
    repository/      # orchestration backend/local
  domain/
    model/
    usecase/
  ui/
    home/
    medications/
    schedule/
    history/
```

## Testing

Pruebas instrumentadas incluidas:

- `MainFlowInstrumentedTest`: navegacion principal y visibilidad de pantallas.
- `ExampleInstrumentedTest`: validacion de package context.

Ejecucion por CLI:

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Roadmap sugerido

- completar persistencia online/offline real con Room como fuente primaria,
- implementar historial funcional de eventos de dosis,
- conectar `MissedDoseWorker` con politicas de gracia y escalamiento,
- agregar `DELETE /schedules/{id}` en backend y sincronizacion bidireccional,
- endurecer manejo de permisos de notificaciones en runtime,
- sumar CI con build + tests instrumentados.

## Licencia

Definir licencia del proyecto (pendiente).
