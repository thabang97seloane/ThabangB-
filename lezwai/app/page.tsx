import Link from "next/link";
import { Footer } from "@/components/Footer";
import { HeroPrompt } from "@/components/HeroPrompt";
import { Navbar } from "@/components/Navbar";
import { PricingCards } from "@/components/PricingCards";

const FEATURES = [
  { icon: "🗺️", title: "AI sitemaps", text: "Describe your business in one line. LEZWAI plans every page and the sections each one needs." },
  { icon: "🧱", title: "Instant wireframes", text: "Each page becomes a clean wireframe built from proven section patterns — hero, features, pricing, FAQ and more." },
  { icon: "✍️", title: "Real copy, not lorem ipsum", text: "Headlines, body text and CTAs are written for your brand so clients see the real thing." },
  { icon: "🎨", title: "Style guide", text: "AI-picked colours, Google Fonts and corner styles. Tweak them and watch every page update live." },
  { icon: "🔁", title: "Edit everything", text: "Rename pages, reorder or swap sections, add new pages — the sitemap is fully editable." },
  { icon: "📦", title: "Export anywhere", text: "Download a responsive HTML site or the project JSON to take into Figma, Webflow or your own code." },
];

const STEPS = [
  { n: "01", title: "Describe", text: "Tell LEZWAI what the site is for, who it's for and how many pages you need." },
  { n: "02", title: "Shape the sitemap", text: "Review the AI-generated structure. Drag sections, rename pages, add what's missing." },
  { n: "03", title: "Style & export", text: "Apply a style guide, preview every page, then export HTML or JSON in one click." },
];

const FAQ = [
  ["What is LEZWAI?", "LEZWAI is an AI website design tool. It turns a short description into a sitemap, wireframes, website copy and a style guide that you can edit and export."],
  ["Do I need to know how to code?", "No. You describe the site in plain language and edit visually. Developers can export the HTML or JSON to build on."],
  ["Can I use LEZWAI for client work?", "Yes — everything you generate is yours to use commercially, on any plan."],
  ["Which AI powers LEZWAI?", "LEZWAI uses Claude by Anthropic to plan sitemaps and write copy."],
  ["Can I cancel anytime?", "Yes. Paid plans are month-to-month (or yearly) and you can cancel from your account page."],
];

export default function Home() {
  return (
    <>
      <Navbar />
      <main>
        {/* Hero */}
        <section className="grid-bg relative overflow-hidden">
          <div className="mx-auto max-w-5xl px-4 pt-20 pb-16 text-center sm:px-6 sm:pt-28">
            <span className="inline-flex items-center gap-2 rounded-full border border-line bg-white px-3 py-1 text-xs font-semibold">
              <span className="h-2 w-2 rounded-full bg-lime" /> New: AI style guides
            </span>
            <h1 className="mt-6 font-display text-5xl leading-[1.05] font-bold tracking-tight sm:text-7xl">
              Websites designed <br className="hidden sm:block" />
              <span className="relative inline-block">
                <span className="relative z-10">in minutes</span>
                <span className="absolute inset-x-0 bottom-1 -z-0 h-4 bg-lime sm:h-6" aria-hidden />
              </span>
              , not weeks.
            </h1>
            <p className="mx-auto mt-6 max-w-2xl text-lg text-muted">
              LEZWAI is the AI website designer. Go from a one-line idea to a full sitemap, wireframes and style guide —
              then export and build.
            </p>
            <HeroPrompt />
            <p className="mt-3 text-xs text-muted">Free to start · No credit card required</p>
          </div>

          {/* Product mock */}
          <div className="mx-auto max-w-6xl px-4 pb-20 sm:px-6">
            <div className="rounded-2xl border border-line bg-white p-3 shadow-2xl">
              <div className="flex items-center gap-1.5 px-2 pb-3">
                <span className="h-3 w-3 rounded-full bg-red-300" />
                <span className="h-3 w-3 rounded-full bg-yellow-300" />
                <span className="h-3 w-3 rounded-full bg-green-300" />
                <span className="ml-3 text-xs text-muted">lezwai.app / Bean There Coffee</span>
              </div>
              <div className="grid-bg overflow-x-auto rounded-xl p-6">
                <div className="flex justify-center">
                  <MockPage name="Home" sections={["Navbar", "Hero", "Features", "Testimonials", "CTA", "Footer"]} highlight />
                </div>
                <div className="mx-auto h-6 w-px bg-ink/30" />
                <div className="mx-auto flex min-w-max justify-center gap-4">
                  <MockPage name="Menu" sections={["Navbar", "Hero", "Gallery", "Footer"]} />
                  <MockPage name="Order online" sections={["Navbar", "Pricing", "FAQ", "Footer"]} />
                  <MockPage name="About" sections={["Navbar", "Hero", "Team", "Footer"]} />
                  <MockPage name="Contact" sections={["Navbar", "Contact", "Footer"]} />
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Logos */}
        <section className="border-y border-line bg-white py-10">
          <p className="text-center text-sm text-muted">Loved by designers, agencies and founders</p>
          <div className="mx-auto mt-5 flex max-w-4xl flex-wrap justify-center gap-x-12 gap-y-4 px-4 font-display text-xl font-bold text-ink/30">
            <span>Northwind</span><span>Kasi Studio</span><span>Umoya</span><span>Brightlane</span><span>Pixelforge</span>
          </div>
        </section>

        {/* Features */}
        <section id="features" className="mx-auto max-w-7xl scroll-mt-20 px-4 py-24 sm:px-6">
          <div className="max-w-2xl">
            <p className="text-sm font-semibold text-violet">Features</p>
            <h2 className="mt-2 font-display text-4xl font-bold tracking-tight">Everything you need to design the site before you build it</h2>
          </div>
          <div className="mt-12 grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
            {FEATURES.map((f) => (
              <div key={f.title} className="rounded-2xl border border-line bg-white p-6 transition hover:-translate-y-1 hover:shadow-lg">
                <span className="text-3xl" aria-hidden>{f.icon}</span>
                <h3 className="mt-4 font-display text-lg font-bold">{f.title}</h3>
                <p className="mt-2 text-sm text-muted">{f.text}</p>
              </div>
            ))}
          </div>
        </section>

        {/* How it works */}
        <section id="how" className="scroll-mt-20 bg-ink py-24 text-white">
          <div className="mx-auto max-w-7xl px-4 sm:px-6">
            <p className="text-sm font-semibold text-lime">How it works</p>
            <h2 className="mt-2 max-w-2xl font-display text-4xl font-bold tracking-tight">From idea to export in three steps</h2>
            <div className="mt-12 grid gap-8 md:grid-cols-3">
              {STEPS.map((s) => (
                <div key={s.n} className="border-t border-white/20 pt-6">
                  <span className="font-display text-5xl font-bold text-lime">{s.n}</span>
                  <h3 className="mt-4 text-xl font-bold">{s.title}</h3>
                  <p className="mt-2 text-white/60">{s.text}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* Testimonials */}
        <section className="mx-auto max-w-7xl px-4 py-24 sm:px-6">
          <h2 className="text-center font-display text-4xl font-bold tracking-tight">Designers are shipping faster</h2>
          <div className="mt-12 grid gap-5 md:grid-cols-3">
            {[
              ["LEZWAI cut our discovery phase from two weeks to an afternoon. Clients approve sitemaps on the first call now.", "Naledi K.", "Creative Director"],
              ["The copy it writes is genuinely usable. I export the JSON and my dev team builds straight from it.", "Sipho D.", "Agency Founder"],
              ["I built my bakery's website plan myself, without hiring anyone. It just made sense.", "Ayesha P.", "Small Business Owner"],
            ].map(([q, n, r]) => (
              <figure key={n} className="rounded-2xl border border-line bg-white p-6">
                <blockquote className="text-lg">“{q}”</blockquote>
                <figcaption className="mt-6 text-sm">
                  <span className="font-semibold">{n}</span> <span className="text-muted">· {r}</span>
                </figcaption>
              </figure>
            ))}
          </div>
        </section>

        {/* Pricing */}
        <section id="pricing" className="scroll-mt-20 border-t border-line bg-white py-24">
          <div className="mx-auto max-w-7xl px-4 sm:px-6">
            <h2 className="text-center font-display text-4xl font-bold tracking-tight">Simple pricing</h2>
            <p className="mt-3 mb-10 text-center text-muted">Start free. Upgrade when you&apos;re ready.</p>
            <PricingCards />
          </div>
        </section>

        {/* FAQ */}
        <section id="faq" className="mx-auto max-w-3xl scroll-mt-20 px-4 py-24 sm:px-6">
          <h2 className="text-center font-display text-4xl font-bold tracking-tight">Questions & answers</h2>
          <div className="mt-10 divide-y divide-line border-y border-line">
            {FAQ.map(([q, a]) => (
              <details key={q} className="group py-5">
                <summary className="flex cursor-pointer list-none items-center justify-between font-semibold">
                  {q}
                  <span className="text-xl transition group-open:rotate-45">+</span>
                </summary>
                <p className="mt-3 text-muted">{a}</p>
              </details>
            ))}
          </div>
        </section>

        {/* CTA */}
        <section className="px-4 pb-24 sm:px-6">
          <div className="mx-auto max-w-6xl rounded-3xl bg-violet px-6 py-16 text-center text-white">
            <h2 className="font-display text-4xl font-bold tracking-tight sm:text-5xl">Design your next website with LEZWAI</h2>
            <p className="mx-auto mt-4 max-w-xl text-white/80">Your first sitemap is free. See it in under a minute.</p>
            <Link href="/app" className="mt-8 inline-block rounded-full bg-lime px-6 py-3 font-semibold text-ink hover:brightness-95">
              Start building — free
            </Link>
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}

function MockPage({ name, sections, highlight }: { name: string; sections: string[]; highlight?: boolean }) {
  return (
    <div className={`w-44 rounded-lg border bg-white text-left shadow-sm ${highlight ? "border-violet" : "border-line"}`}>
      <p className="border-b border-line px-3 py-2 text-xs font-bold">{name}</p>
      <ul className="space-y-1 p-2">
        {sections.map((s) => (
          <li key={s} className="rounded bg-paper px-2 py-1 text-[11px] text-muted">{s}</li>
        ))}
      </ul>
    </div>
  );
}
