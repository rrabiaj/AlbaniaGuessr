// Static file server for the built AlbaniaGuessr frontend.
// Serves files from dist/ on port 3000, with SPA fallback to index.html.
import { $ } from "bun";

const PORT = 3000;
const HOST = "0.0.0.0";
const DIST_DIR = `${import.meta.dir}/dist`;

const freePort =
  `for _ in $(seq 1 25); do ` +
  `pids=$(lsof -t -iTCP:${String(PORT)} -sTCP:LISTEN 2>/dev/null || true); ` +
  `if [ -z "$pids" ]; then exit 0; fi; ` +
  `kill $pids 2>/dev/null || true; sleep 0.2; ` +
  `done`;

for (let attempt = 1; ; attempt++) {
  await $`sudo sh -c ${freePort}`.quiet().nothrow();
  try {
    Bun.serve({
      port: PORT,
      hostname: HOST,
      async fetch(req) {
        const url = new URL(req.url);
        let filePath = DIST_DIR + url.pathname;
        let file = Bun.file(filePath);
        if (await file.exists()) {
          return new Response(file);
        }
        // SPA fallback: serve index.html for all non-file routes
        file = Bun.file(DIST_DIR + "/index.html");
        if (await file.exists()) {
          return new Response(file, {
            headers: { "Content-Type": "text/html" },
          });
        }
        return new Response("Not found", { status: 404 });
      },
    });
    break;
  } catch (err) {
    if (attempt >= 10) throw err;
    await Bun.sleep(200);
  }
}

console.log(`AlbaniaGuessr serving on http://${HOST}:${String(PORT)}`);