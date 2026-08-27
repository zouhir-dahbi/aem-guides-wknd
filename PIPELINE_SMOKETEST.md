# Pipeline smoketest branch

This branch exists to exercise Flow Manager / Zubin DAG pipeline mechanics
(checkout, build, package, deploy) without risking impact to any tenant's
real AEM content.

## What changed vs `main`

`ui.content.sample/.../META-INF/vault/filter.xml` — the sample content
package's filters were switched from replace mode to `mode="merge"` on
`/content/wknd`, `/content/experience-fragments/wknd`, and
`/home/users/wknd`. Upstream, these filters replace (i.e. delete) anything
under those roots that isn't part of the sample package. On a tenant that
already has real content there, that's destructive. Merge mode only
adds/updates the sample content and never deletes anything pre-existing.

`/content/dam/wknd` and `/home/groups/wknd` were already merge mode.
`ui.content` (non-sample) was already fully merge mode. `ui.apps` filters
only touch `/apps/wknd/*` (code), not customer content.

`dispatcher` was dropped from the root `pom.xml` reactor. Dispatcher
deploys in AEMaaCS are a full atomic replacement of the customer's live
vhost/farm/filter/cache config — there's no merge mode, and a standard FM
build&deploy has no way to exclude the dispatcher target once it's part of
the build. Dropping the module means a standard build&deploy off this
branch never produces a dispatcher package, so nothing dispatcher-related
gets touched. Test dispatcher build/deploy separately, via a dedicated
dispatcher-only FM pipeline, against a disposable environment only.

## Usage

Safe to build and deploy the `all` package from this branch against any
tenant/environment — deploying will only add/update the standard WKND
sample content, never remove pre-existing content under `/content/wknd`,
`/content/dam/wknd`, `/content/experience-fragments/wknd`, or
`/home/users/wknd`.

Do not merge this branch into `main`; keep it as a standalone target for
pipeline testing.
