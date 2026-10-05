"use client";

import Link from "next/link";
import { useEffect, useMemo, useState } from "react";
import { Logo } from "@/components/Logo";
import { SectionBlock } from "@/components/SectionBlock";
import { exportHtml } from "@/lib/exportHtml";
import { FONT_OPTIONS, SECTION_TYPES, type Page, type SectionType, type Site } from "@/lib/schema";

type Tab = "sitemap" | "wireframe" | "style" | "export";
const STORAGE_KEY = "lezwai:project";

const EXAMPLES = [
  "A modern coffee shop in Johannesburg called \"Bean There\" with online ordering",
  "Portfolio for a freelance brand designer who works with startups",
  "SaaS landing site for an AI tool that helps teachers mark homework",
  "Online store selling handmade African print clothing",
];

function blankSection(type: SectionType) {
  return { type, heading: `New ${type} section`, body: "", items: [], cta: "" };
}

function download(name: string, content: string, type: string) {
  const url = URL.createObjectURL(new Blob([content], { type }));
  const a = document.createElement("a");
  a.href = url;
  a.download = name;
  a.click();
  URL.revokeObjectURL(url);
}

export default function BuilderPage() {
  const [prompt, setPrompt] = useState("");
  const [pageCount, setPageCount] = useState(5);
  const [site, setSite] = useState<Site | null>(null);
  const [mode, setMode] = useState<"ai" | "demo" | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [tab, setTab] = useState<Tab>("sitemap");
  const [activePage, setActivePage] = useState(0);
  const [styled, setStyled] = useState(false);

  // Restore the last project, or pick up a prompt passed from the landing page.
  useEffect(() => {
    const q = new URLSearchParams(window.location.search).get("prompt");
    if (q) {
      setPrompt(q);
      void generate(q);
      return;
    }
    try {
      const saved = localStorage.getItem(STORAGE_KEY);
      if (saved) {
        const data = JSON.parse(saved);
        setSite(data.site);
        setPrompt(data.prompt ?? "");
      }
    } catch {}
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (!site) return;
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify({ site, prompt }));
    } catch {}
  }, [site, prompt]);

  async function generate(text = prompt) {
    if (!text.trim()) return;
    setLoading(true);
    setError("");
    try {
      const res = await fetch("/api/generate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ prompt: text, pageCount }),
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || "Generation failed");
      setSite(data.site);
      setMode(data.mode);
      setActivePage(0);
      setTab("sitemap");
    } catch (e) {
      setError(e instanceof Error ? e.message : "Generation failed");
    } finally {
      setLoading(false);
    }
  }

  function updatePage(idx: number, fn: (p: Page) => Page) {
    setSite((s) => (s ? { ...s, pages: s.pages.map((p, i) => (i === idx ? fn(p) : p)) } : s));
  }

  function moveSection(pi: number, si: number, dir: -1 | 1) {
    updatePage(pi, (p) => {
      const sections = [...p.sections];
      const j = si + dir;
      if (j < 0 || j >= sections.length) return p;
      [sections[si], sections[j]] = [sections[j], sections[si]];
      return { ...p, sections };
    });
  }

  if (!site) {
    return (
      <div className="min-h-screen grid-bg">
        <header className="flex h-16 items-center justify-between border-b border-line bg-paper px-4 sm:px-6">
          <Logo />
          <Link href="/app/manage-account/plan" className="text-sm font-medium hover:underline">My plan</Link>
        </header>
        <main className="mx-auto max-w-2xl px-4 py-16">
          <h1 className="text-center font-display text-4xl font-bold tracking-tight sm:text-5xl">What are we building today?</h1>
          <p className="mt-3 text-center text-muted">Describe the business or project. LEZWAI plans the sitemap, writes the copy and wireframes every page.</p>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              void generate();
            }}
            className="mt-8 rounded-2xl border border-line bg-white p-4 shadow-sm"
          >
            <label htmlFor="prompt" className="sr-only">Website description</label>
            <textarea
              id="prompt"
              value={prompt}
              onChange={(e) => setPrompt(e.target.value)}
              rows={4}
              placeholder="e.g. A website for a yoga studio in Cape Town with class schedules and online booking"
              className="w-full resize-none bg-transparent text-base outline-none"
            />
            <div className="mt-3 flex flex-wrap items-center justify-between gap-3">
              <label className="flex items-center gap-2 text-sm text-muted">
                Pages
                <select
                  value={pageCount}
                  onChange={(e) => setPageCount(Number(e.target.value))}
                  className="rounded-md border border-line bg-white px-2 py-1 text-ink"
                >
                  {[1, 3, 5, 6, 8, 10].map((n) => <option key={n}>{n}</option>)}
                </select>
              </label>
              <button
                disabled={loading || !prompt.trim()}
                className="rounded-full bg-violet px-5 py-2.5 text-sm font-semibold text-white hover:brightness-110 disabled:opacity-50"
              >
                {loading ? "Generating…" : "✦ Generate sitemap"}
              </button>
            </div>
          </form>
          {error && <p className="mt-4 rounded-lg bg-red-50 p-3 text-sm text-red-700">{error}</p>}
          <div className="mt-8">
            <p className="text-xs font-semibold uppercase tracking-wider text-muted">Try an example</p>
            <div className="mt-3 flex flex-wrap gap-2">
              {EXAMPLES.map((ex) => (
                <button key={ex} onClick={() => setPrompt(ex)} className="rounded-full border border-line bg-white px-3 py-1.5 text-left text-sm hover:border-ink">
                  {ex}
                </button>
              ))}
            </div>
          </div>
        </main>
      </div>
    );
  }

  return (
    <div className="flex min-h-screen flex-col bg-[#f3f2ee]">
      <header className="sticky top-0 z-30 border-b border-line bg-paper">
        <div className="flex h-14 items-center justify-between gap-4 px-4">
          <div className="flex min-w-0 items-center gap-4">
            <Logo />
            <span className="hidden truncate text-sm text-muted sm:inline">/ {site.siteName}</span>
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={() => {
                setSite(null);
                setMode(null);
              }}
              className="rounded-full border border-line px-3 py-1.5 text-sm font-medium hover:border-ink"
            >
              New project
            </button>
            <Link href="/app/manage-account/plan" className="hidden rounded-full bg-ink px-3 py-1.5 text-sm font-semibold text-white sm:inline">
              Upgrade
            </Link>
          </div>
        </div>
        <nav className="flex gap-1 overflow-x-auto px-4" aria-label="Builder steps">
          {(
            [
              ["sitemap", "1. Sitemap"],
              ["wireframe", "2. Wireframe"],
              ["style", "3. Style guide"],
              ["export", "4. Export"],
            ] as [Tab, string][]
          ).map(([id, label]) => (
            <button
              key={id}
              onClick={() => {
                setTab(id);
                if (id === "wireframe") setStyled(false);
              }}
              className={`whitespace-nowrap border-b-2 px-3 py-2.5 text-sm font-medium ${tab === id ? "border-violet text-ink" : "border-transparent text-muted hover:text-ink"}`}
            >
              {label}
            </button>
          ))}
        </nav>
      </header>

      {mode === "demo" && (
        <div className="bg-lime/40 px-4 py-2 text-center text-xs">
          Demo mode: add <code className="font-semibold">ANTHROPIC_API_KEY</code> to your environment for fully AI-written sitemaps and copy.
        </div>
      )}

      {/* Regenerate bar */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          void generate();
        }}
        className="flex flex-wrap items-center gap-2 border-b border-line bg-white px-4 py-2"
      >
        <input
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          className="min-w-0 flex-1 rounded-md border border-line px-3 py-1.5 text-sm"
          aria-label="Website description"
        />
        <button disabled={loading} className="rounded-full bg-violet px-4 py-1.5 text-sm font-semibold text-white disabled:opacity-50">
          {loading ? "Generating…" : "Regenerate"}
        </button>
        {error && <span className="w-full text-sm text-red-700">{error}</span>}
      </form>

      <main className="flex-1 overflow-auto">
        {tab === "sitemap" && (
          <SitemapView
            site={site}
            setSite={setSite}
            updatePage={updatePage}
            moveSection={moveSection}
            openPage={(i) => {
              setActivePage(i);
              setTab("wireframe");
            }}
          />
        )}
        {(tab === "wireframe" || tab === "style") && (
          <div className="flex flex-col lg:flex-row">
            <aside className="border-b border-line bg-white p-4 lg:w-72 lg:shrink-0 lg:border-r lg:border-b-0">
              {tab === "wireframe" ? (
                <>
                  <p className="text-xs font-semibold uppercase tracking-wider text-muted">Pages</p>
                  <div className="mt-2 flex gap-1 overflow-x-auto lg:flex-col">
                    {site.pages.map((p, i) => (
                      <button
                        key={p.slug + i}
                        onClick={() => setActivePage(i)}
                        className={`whitespace-nowrap rounded-md px-3 py-2 text-left text-sm ${i === activePage ? "bg-violet-soft font-semibold text-violet" : "hover:bg-paper"}`}
                      >
                        {p.name}
                      </button>
                    ))}
                  </div>
                  <label className="mt-4 flex items-center gap-2 text-sm">
                    <input type="checkbox" checked={styled} onChange={(e) => setStyled(e.target.checked)} />
                    Apply style guide
                  </label>
                </>
              ) : (
                <StyleEditor site={site} setSite={setSite} />
              )}
            </aside>
            <div className="flex-1 p-4 sm:p-8">
              <div className="mx-auto max-w-5xl overflow-hidden rounded-xl border border-line bg-white shadow-sm">
                <FontLoader site={site} />
                {site.pages[activePage]?.sections.map((s, i) => (
                  <div key={i} className="group relative">
                    <span className="absolute top-1 right-2 z-10 rounded bg-ink/80 px-2 py-0.5 text-[10px] font-semibold uppercase text-white opacity-0 group-hover:opacity-100">
                      {s.type}
                    </span>
                    <SectionBlock section={s} style={site.style} styled={tab === "style" || styled} />
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}
        {tab === "export" && <ExportView site={site} />}
      </main>
    </div>
  );
}

function FontLoader({ site }: { site: Site }) {
  const href = useMemo(() => {
    const fams = Array.from(new Set([site.style.headingFont, site.style.bodyFont]))
      .map((f) => `family=${encodeURIComponent(f).replace(/%20/g, "+")}:wght@400;600;700`)
      .join("&");
    return `https://fonts.googleapis.com/css2?${fams}&display=swap`;
  }, [site.style.headingFont, site.style.bodyFont]);
  // eslint-disable-next-line @next/next/no-page-custom-font
  return <link rel="stylesheet" href={href} />;
}

function SitemapView({
  site,
  setSite,
  updatePage,
  moveSection,
  openPage,
}: {
  site: Site;
  setSite: React.Dispatch<React.SetStateAction<Site | null>>;
  updatePage: (i: number, fn: (p: Page) => Page) => void;
  moveSection: (pi: number, si: number, dir: -1 | 1) => void;
  openPage: (i: number) => void;
}) {
  return (
    <div className="grid-bg min-h-full p-4 sm:p-8">
      <div className="flex gap-5 overflow-x-auto pb-6">
        {site.pages.map((page, pi) => (
          <div key={pi} className="w-72 shrink-0">
            <div className={`rounded-xl border bg-white shadow-sm ${pi === 0 ? "border-violet" : "border-line"}`}>
              <div className="flex items-center gap-2 border-b border-line p-3">
                <input
                  value={page.name}
                  onChange={(e) => updatePage(pi, (p) => ({ ...p, name: e.target.value }))}
                  className="min-w-0 flex-1 font-display font-bold outline-none"
                  aria-label="Page name"
                />
                <button onClick={() => openPage(pi)} className="text-xs font-semibold text-violet hover:underline">View</button>
                {site.pages.length > 1 && (
                  <button
                    onClick={() => setSite((s) => (s ? { ...s, pages: s.pages.filter((_, i) => i !== pi) } : s))}
                    className="text-muted hover:text-red-600"
                    aria-label={`Delete ${page.name}`}
                  >
                    ×
                  </button>
                )}
              </div>
              <p className="px-3 pt-2 text-xs text-muted">{page.slug}</p>
              <ul className="space-y-1.5 p-3">
                {page.sections.map((sec, si) => (
                  <li key={si} className="group rounded-lg border border-line bg-paper p-2">
                    <div className="flex items-center gap-1">
                      <span className="rounded bg-violet-soft px-1.5 py-0.5 text-[10px] font-bold uppercase text-violet">{sec.type}</span>
                      <span className="ml-auto flex gap-1 opacity-0 group-hover:opacity-100 focus-within:opacity-100">
                        <button onClick={() => moveSection(pi, si, -1)} aria-label="Move up" className="px-1 text-xs">↑</button>
                        <button onClick={() => moveSection(pi, si, 1)} aria-label="Move down" className="px-1 text-xs">↓</button>
                        <button
                          onClick={() => updatePage(pi, (p) => ({ ...p, sections: p.sections.filter((_, i) => i !== si) }))}
                          aria-label="Remove section"
                          className="px-1 text-xs hover:text-red-600"
                        >
                          ×
                        </button>
                      </span>
                    </div>
                    <input
                      value={sec.heading}
                      onChange={(e) =>
                        updatePage(pi, (p) => ({
                          ...p,
                          sections: p.sections.map((x, i) => (i === si ? { ...x, heading: e.target.value } : x)),
                        }))
                      }
                      className="mt-1 w-full bg-transparent text-sm outline-none"
                      aria-label="Section heading"
                    />
                  </li>
                ))}
              </ul>
              <div className="border-t border-line p-3">
                <select
                  value=""
                  onChange={(e) => {
                    const type = e.target.value as SectionType;
                    if (!type) return;
                    updatePage(pi, (p) => {
                      const sections = [...p.sections];
                      const at = sections.at(-1)?.type === "footer" ? sections.length - 1 : sections.length;
                      sections.splice(at, 0, blankSection(type));
                      return { ...p, sections };
                    });
                  }}
                  className="w-full rounded-md border border-dashed border-line bg-white px-2 py-1.5 text-sm text-muted"
                  aria-label="Add section"
                >
                  <option value="">+ Add section</option>
                  {SECTION_TYPES.map((t) => <option key={t} value={t}>{t}</option>)}
                </select>
              </div>
            </div>
          </div>
        ))}
        <button
          onClick={() =>
            setSite((s) =>
              s
                ? {
                    ...s,
                    pages: [
                      ...s.pages,
                      {
                        name: "New page",
                        slug: `/page-${s.pages.length + 1}`,
                        sections: [
                          { ...s.pages[0].sections[0] },
                          blankSection("hero"),
                          ...(s.pages[0].sections.at(-1)?.type === "footer" ? [{ ...s.pages[0].sections.at(-1)! }] : []),
                        ],
                      },
                    ],
                  }
                : s,
            )
          }
          className="grid h-32 w-48 shrink-0 place-items-center rounded-xl border-2 border-dashed border-line text-sm font-medium text-muted hover:border-ink hover:text-ink"
        >
          + Add page
        </button>
      </div>
    </div>
  );
}

function StyleEditor({ site, setSite }: { site: Site; setSite: React.Dispatch<React.SetStateAction<Site | null>> }) {
  const st = site.style;
  const set = (patch: Partial<Site["style"]>) => setSite((s) => (s ? { ...s, style: { ...s.style, ...patch } } : s));
  const colors: [keyof Site["style"], string][] = [
    ["primary", "Primary"],
    ["secondary", "Secondary"],
    ["background", "Background"],
    ["text", "Text"],
  ];
  return (
    <div className="space-y-5">
      <div>
        <p className="text-xs font-semibold uppercase tracking-wider text-muted">Colours</p>
        <div className="mt-2 grid grid-cols-2 gap-2">
          {colors.map(([key, label]) => (
            <label key={key} className="flex items-center gap-2 rounded-md border border-line p-2 text-xs">
              <input type="color" value={st[key] as string} onChange={(e) => set({ [key]: e.target.value })} className="h-7 w-7 cursor-pointer rounded border-0 bg-transparent p-0" />
              <span>
                {label}
                <br />
                <span className="text-muted">{st[key]}</span>
              </span>
            </label>
          ))}
        </div>
      </div>
      {(["headingFont", "bodyFont"] as const).map((key) => (
        <label key={key} className="block text-sm">
          <span className="text-xs font-semibold uppercase tracking-wider text-muted">{key === "headingFont" ? "Heading font" : "Body font"}</span>
          <select value={st[key]} onChange={(e) => set({ [key]: e.target.value })} className="mt-1 w-full rounded-md border border-line bg-white px-2 py-1.5">
            {Array.from(new Set([st[key], ...FONT_OPTIONS])).map((f) => <option key={f}>{f}</option>)}
          </select>
        </label>
      ))}
      <div>
        <p className="text-xs font-semibold uppercase tracking-wider text-muted">Corners</p>
        <div className="mt-2 flex gap-1">
          {(["none", "small", "large"] as const).map((r) => (
            <button key={r} onClick={() => set({ radius: r })} className={`flex-1 rounded-md border px-2 py-1.5 text-sm capitalize ${st.radius === r ? "border-violet bg-violet-soft text-violet" : "border-line"}`}>
              {r}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}

function ExportView({ site }: { site: Site }) {
  const [copied, setCopied] = useState(false);
  const slug = site.siteName.toLowerCase().replace(/[^a-z0-9]+/g, "-") || "site";
  const json = JSON.stringify(site, null, 2);
  return (
    <div className="mx-auto max-w-4xl p-4 sm:p-8">
      <h2 className="font-display text-2xl font-bold">Export {site.siteName}</h2>
      <p className="mt-1 text-muted">Take your design anywhere.</p>
      <div className="mt-6 grid gap-4 sm:grid-cols-3">
        <ExportCard title="HTML website" desc="A responsive, single-file site with every page. Open it in any browser or upload to any host." onClick={() => download(`${slug}.html`, exportHtml(site), "text/html")} cta="Download .html" />
        <ExportCard title="Project JSON" desc="The full sitemap, copy and style guide. Re-import later or feed it into your own code." onClick={() => download(`${slug}.json`, json, "application/json")} cta="Download .json" />
        <ExportCard
          title="Copy to clipboard"
          desc="Paste the JSON into Figma plugins, Webflow, or a developer's handoff doc."
          onClick={async () => {
            await navigator.clipboard.writeText(json);
            setCopied(true);
            setTimeout(() => setCopied(false), 1500);
          }}
          cta={copied ? "Copied ✓" : "Copy JSON"}
        />
      </div>
      <pre className="mt-6 max-h-96 overflow-auto rounded-xl bg-ink p-4 text-xs text-white/80">{json}</pre>
    </div>
  );
}

function ExportCard({ title, desc, cta, onClick }: { title: string; desc: string; cta: string; onClick: () => void }) {
  return (
    <div className="flex flex-col rounded-xl border border-line bg-white p-5">
      <h3 className="font-semibold">{title}</h3>
      <p className="mt-1 flex-1 text-sm text-muted">{desc}</p>
      <button onClick={onClick} className="mt-4 rounded-full bg-ink px-4 py-2 text-sm font-semibold text-white hover:bg-ink-soft">{cta}</button>
    </div>
  );
}
