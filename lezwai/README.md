# LEZWAI — AI Website Designer

LEZWAI turns a one-line description into a **sitemap → wireframes → style guide → export**, similar to Relume.

| Route | What it is |
|---|---|
| `/` | Marketing landing page (hero prompt, features, how it works, pricing, FAQ) |
| `/app` | The builder: prompt → editable sitemap → wireframes → style guide → export (HTML / JSON) |
| `/pricing` | Pricing page (Free, Starter, Pro, Team; monthly/yearly toggle) |
| `/app/manage-account/plan` | Plan & billing page with Stripe Checkout upgrade buttons |
| `/api/generate` | Calls Claude to generate the site plan (falls back to a demo generator without a key) |
| `/api/checkout` | Creates a Stripe Checkout subscription session |

Stack: Next.js 16 (App Router) · React 19 · Tailwind CSS 4 · Anthropic SDK (Claude) · Stripe.

---

## 1. Run it on your computer

You need **Node.js 20+** (https://nodejs.org).

```bash
cd lezwai
npm install
cp .env.example .env.local   # then fill in the keys (see below)
npm run dev                  # open http://localhost:3000
```

It works immediately in **demo mode** (template-based sitemaps) even with no keys.

## 2. Things you need to connect

### a) Claude API key — required for real AI generation
1. Go to https://console.anthropic.com → sign up → **Billing**: add credit.
2. **API Keys → Create key**, copy it.
3. Put it in `.env.local`: `ANTHROPIC_API_KEY=sk-ant-...`
4. Restart `npm run dev`. The "Demo mode" banner disappears and Claude writes the sitemap, copy and style guide.

The model is set in `app/api/generate/route.ts` (`claude-opus-5-5`). Server-side fallback is enabled, so if
a request is declined by a safety classifier the API retries it on a fallback model automatically.

### b) Stripe — required to take payments
1. Create an account at https://dashboard.stripe.com (stay in **Test mode** while building).
2. **Product catalogue → Add product** three times: *Starter* ($19/mo), *Pro* ($39/mo), *Team* ($79/mo), each with a **recurring monthly** price.
   (Optional: add yearly prices too.)
3. Copy each **Price ID** (`price_...`) into `.env.local` as `STRIPE_PRICE_STARTER`, `STRIPE_PRICE_PRO`, `STRIPE_PRICE_TEAM`.
4. **Developers → API keys**: copy the secret key into `STRIPE_SECRET_KEY`.
5. Set `NEXT_PUBLIC_SITE_URL` to your live domain once deployed.
6. Test with card `4242 4242 4242 4242`, any future date, any CVC.
7. Turn on the **Customer portal** (Settings → Billing → Customer portal) so users can cancel/change plans.

### c) Hosting — put it online (Vercel, free tier is fine)
1. Push this repo to GitHub (already done if you're reading this there).
2. Go to https://vercel.com → **Add New Project** → import the repo.
3. Set **Root Directory** to `lezwai`.
4. Add the environment variables from `.env.example` under **Settings → Environment Variables**.
5. Deploy. You get a `*.vercel.app` URL.

### d) Domain
Buy a domain (e.g. `lezwai.com` / `lezwai.co.za`) from Namecheap, GoDaddy, Cloudflare or Domains.co.za, then in
Vercel **Settings → Domains** add it and follow the DNS instructions. Update `NEXT_PUBLIC_SITE_URL`.

## 3. What to add next (not built yet)

These are the pieces a production SaaS like Relume needs that this starter does not include yet:

| Need | Recommended service | Why |
|---|---|---|
| **User accounts / login** | [Clerk](https://clerk.com) or [Supabase Auth](https://supabase.com) | Right now projects are saved only in the visitor's browser and everyone is shown the Free plan. |
| **Database** | [Supabase](https://supabase.com) (Postgres) or [Neon](https://neon.tech) | Save projects per user and store which plan each user is on. |
| **Stripe webhook** | `POST /api/stripe/webhook` listening for `checkout.session.completed` and `customer.subscription.updated/deleted` | Mark the user's plan as paid in the database. Without this, a payment isn't linked to an account. |
| **Plan limits** | Check the user's plan in `/api/generate` | Enforce "1 project on Free", page limits, etc. |
| **Rate limiting** | [Upstash Ratelimit](https://upstash.com) | Stop people spending your Claude credits by spamming the generate endpoint. |
| **Email** | [Resend](https://resend.com) | Welcome / receipt emails. |
| **Analytics** | Vercel Analytics or Plausible | See who visits and converts. |
| **Legal pages** | Privacy Policy & Terms of Service | Required before taking payments (Stripe and POPIA in South Africa). |

## 4. Before you launch — replace placeholder content

The landing page (`app/page.tsx`) contains **sample testimonials and customer logos** ("Naledi K.", "Northwind", etc.)
so you can see the layout. They are not real — replace them with real customer quotes or remove them before going live.
Prices in `lib/plans.ts` are examples; change them to whatever you want to charge (and match them in Stripe).

## Project structure

```
lezwai/
├─ app/
│  ├─ page.tsx                        # landing page
│  ├─ pricing/page.tsx
│  ├─ app/page.tsx                    # the builder
│  ├─ app/manage-account/plan/page.tsx
│  └─ api/{generate,checkout}/route.ts
├─ components/                        # Navbar, Footer, PricingCards, SectionBlock (wireframe renderer)…
└─ lib/
   ├─ schema.ts                       # Site / Page / Section / Style types (Zod)
   ├─ demo.ts                         # no-API-key fallback generator
   ├─ exportHtml.ts                   # HTML export
   └─ plans.ts                        # pricing plans
```
