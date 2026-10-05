import type { Section, Site } from "./schema";

const RADIUS = { none: "0px", small: "6px", large: "16px" } as const;

function esc(s: string) {
  return s.replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[c]!);
}

function pageId(slug: string) {
  return slug === "/" ? "home" : slug.replace(/^\//, "").replace(/[^\w-]/g, "-");
}

function renderSection(s: Section, site: Site): string {
  const list = (cls: string) => s.items.map((i) => `<div class="${cls}">${esc(i)}</div>`).join("");
  const btn = s.cta ? `<a class="btn" href="#">${esc(s.cta)}</a>` : "";
  const head = `<h2>${esc(s.heading)}</h2>${s.body ? `<p class="muted">${esc(s.body)}</p>` : ""}`;
  switch (s.type) {
    case "navbar": {
      const links = site.pages.map((p) => `<a href="#${pageId(p.slug)}">${esc(p.name)}</a>`).join("");
      return `<header class="nav wrap"><strong class="brand">${esc(s.heading)}</strong><nav>${links}</nav>${btn}</header>`;
    }
    case "hero":
      return `<section class="hero wrap"><div><h1>${esc(s.heading)}</h1>${s.body ? `<p class="muted">${esc(s.body)}</p>` : ""}${btn}</div><div class="media"></div></section>`;
    case "logos":
      return `<section class="wrap center"><p class="muted">${esc(s.heading)}</p><div class="logos">${list("logo")}</div></section>`;
    case "faq":
      return `<section class="wrap narrow">${head}${s.items.map((i) => `<details><summary>${esc(i)}</summary><p class="muted">Answer coming soon.</p></details>`).join("")}</section>`;
    case "contact":
      return `<section class="wrap two">${`<div>${head}</div>`}<form onsubmit="event.preventDefault()">${s.items.map((i) => `<label>${esc(i)}<input /></label>`).join("")}<button class="btn">${esc(s.cta || "Send")}</button></form></section>`;
    case "cta":
      return `<section class="wrap"><div class="cta">${head}${s.cta ? `<a class="btn alt" href="#">${esc(s.cta)}</a>` : ""}</div></section>`;
    case "footer":
      return `<footer class="wrap foot"><strong>${esc(s.heading)}</strong><div class="links">${s.items.map((i) => `<a href="#">${esc(i)}</a>`).join("")}</div><small class="muted">${esc(s.body)}</small></footer>`;
    case "testimonials":
      return `<section class="wrap">${head}<div class="grid">${list("card quote")}</div></section>`;
    default:
      return `<section class="wrap">${head}<div class="grid">${s.items.map((i) => `<div class="card"><div class="media sm"></div><h3>${esc(i)}</h3></div>`).join("")}</div>${s.type === "pricing" ? `<div class="center">${btn}</div>` : ""}</section>`;
  }
}

export function exportHtml(site: Site): string {
  const st = site.style;
  const r = RADIUS[st.radius];
  const fonts = Array.from(new Set([st.headingFont, st.bodyFont]))
    .map((f) => `family=${encodeURIComponent(f).replace(/%20/g, "+")}:wght@400;600;700`)
    .join("&");
  const pages = site.pages
    .map((p, idx) => `<main id="${pageId(p.slug)}" class="page${idx === 0 ? " active" : ""}">${p.sections.map((s) => renderSection(s, site)).join("\n")}</main>`)
    .join("\n");

  return `<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>${esc(site.siteName)}</title>
<meta name="description" content="${esc(site.tagline)}" />
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?${fonts}&display=swap" />
<style>
:root{--p:${st.primary};--s:${st.secondary};--bg:${st.background};--t:${st.text};--r:${r}}
*{box-sizing:border-box}body{margin:0;background:var(--bg);color:var(--t);font-family:'${st.bodyFont}',sans-serif;line-height:1.6}
h1,h2,h3,.brand{font-family:'${st.headingFont}',sans-serif;line-height:1.2}h1{font-size:clamp(2rem,5vw,3.25rem);margin:0 0 1rem}h2{font-size:1.75rem;margin:0 0 .5rem}
a{color:inherit}.wrap{max-width:1120px;margin:0 auto;padding:3rem 1rem}.narrow{max-width:720px}.center{text-align:center}.muted{opacity:.72}
.nav{display:flex;align-items:center;justify-content:space-between;gap:1rem;padding:1rem;flex-wrap:wrap}.nav nav{display:flex;gap:1.25rem;flex-wrap:wrap}.nav nav a{text-decoration:none;opacity:.8}
.btn{display:inline-block;background:var(--p);color:#fff;padding:.7rem 1.2rem;border-radius:var(--r);text-decoration:none;font-weight:600;border:0;cursor:pointer;margin-top:1rem}.btn.alt{background:var(--s);color:var(--t)}
.hero{display:grid;grid-template-columns:1fr 1fr;gap:2rem;align-items:center}.media{aspect-ratio:4/3;border-radius:var(--r);background:linear-gradient(135deg,color-mix(in srgb,var(--p) 25%,transparent),color-mix(in srgb,var(--s) 30%,transparent))}.media.sm{aspect-ratio:16/9}
.grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:1.25rem;margin-top:2rem}.card{border:1px solid color-mix(in srgb,var(--t) 14%,transparent);border-radius:var(--r);padding:1.25rem}.quote{font-style:italic}
.logos{display:flex;gap:2rem;justify-content:center;flex-wrap:wrap;font-weight:700;opacity:.5}.cta{background:var(--p);color:#fff;border-radius:var(--r);padding:3rem 1.5rem;text-align:center}
details{border-bottom:1px solid color-mix(in srgb,var(--t) 14%,transparent);padding:.9rem 0}summary{cursor:pointer;font-weight:600}
.two{display:grid;grid-template-columns:1fr 1fr;gap:2rem}form label{display:block;margin-bottom:.8rem;font-size:.9rem}form input{display:block;width:100%;padding:.6rem;border-radius:var(--r);border:1px solid color-mix(in srgb,var(--t) 25%,transparent);background:transparent;color:inherit}
.foot{display:flex;flex-wrap:wrap;gap:1rem;justify-content:space-between;border-top:1px solid color-mix(in srgb,var(--t) 14%,transparent)}.foot .links{display:flex;gap:1rem}.foot small{width:100%}
.page{display:none}.page.active{display:block}
@media(max-width:720px){.hero,.two{grid-template-columns:1fr}}
</style>
</head>
<body>
${pages}
<script>
function show(){var id=(location.hash||'#home').slice(1);var el=document.getElementById(id);if(!el)return;document.querySelectorAll('.page').forEach(function(p){p.classList.toggle('active',p===el)});window.scrollTo(0,0)}
window.addEventListener('hashchange',show);show();
</script>
<!-- Designed with LEZWAI -->
</body>
</html>`;
}
