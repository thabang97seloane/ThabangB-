import type { Page, Section, SectionType, Site } from "./schema";

// Template-based generator used when no ANTHROPIC_API_KEY is configured,
// so the builder works end-to-end out of the box.

function section(type: SectionType, heading: string, body = "", items: string[] = [], cta = ""): Section {
  return { type, heading, body, items, cta };
}

function titleCase(s: string) {
  return s.replace(/\b\w/g, (c) => c.toUpperCase());
}

function guessName(prompt: string) {
  const quoted = prompt.match(/["“']([^"”']{2,40})["”']/);
  if (quoted) return quoted[1];
  const called = prompt.match(/(?:called|named)\s+([A-Z][\w&-]*(?:\s+[A-Z][\w&-]*)?)/);
  if (called) return called[1];
  const words = prompt.split(/\s+/).filter((w) => w.length > 3).slice(0, 2);
  return words.length ? titleCase(words.join(" ")) : "My Website";
}

const PALETTES = [
  { primary: "#6d28d9", secondary: "#f59e0b", background: "#ffffff", text: "#111827" },
  { primary: "#0f766e", secondary: "#f97316", background: "#fafaf9", text: "#1c1917" },
  { primary: "#1d4ed8", secondary: "#10b981", background: "#ffffff", text: "#0f172a" },
  { primary: "#be123c", secondary: "#0ea5e9", background: "#fffbf5", text: "#1f2937" },
];

export function demoSite(prompt: string, pageCount = 5): Site {
  const name = guessName(prompt);
  const raw = prompt.trim().replace(/\.$/, "");
  const topic = raw ? raw[0].toLowerCase() + raw.slice(1) : "a modern business";
  const lower = prompt.toLowerCase();
  const isShop = /shop|store|ecommerce|e-commerce|product/.test(lower);
  const isAgency = /agency|studio|consult|freelanc|portfolio/.test(lower);
  const palette = PALETTES[Math.abs(hash(prompt)) % PALETTES.length];

  const nav = section("navbar", name, "", ["Home", "About", isShop ? "Shop" : "Services", "Pricing", "Contact"], "Get started");
  const footer = section("footer", name, `© ${new Date().getFullYear()} ${name}. All rights reserved.`, ["Privacy", "Terms", "Instagram", "LinkedIn"]);

  const home: Page = {
    name: "Home",
    slug: "/",
    sections: [
      nav,
      section("hero", `${name} — built for what matters to you`, `Everything you need for ${topic}, in one place.`, [], "Get started"),
      section("logos", "Trusted by teams everywhere", "", ["Acme", "Globex", "Initech", "Umbrella", "Hooli"]),
      section("features", "Why choose us", "A few reasons customers love working with us.", ["Fast and reliable", "Friendly support", "Fair, transparent pricing"]),
      section("stats", "Results that speak", "", ["10k+ customers", "99.9% uptime", "4.9★ rating"]),
      section("testimonials", "What people say", "", ["“Absolutely changed how we work.” — Lerato M.", "“Best decision we made this year.” — James K."]),
      section("cta", "Ready to begin?", "Join thousands of happy customers today.", [], "Start now"),
      footer,
    ],
  };

  const about: Page = {
    name: "About",
    slug: "/about",
    sections: [
      nav,
      section("hero", `About ${name}`, `Our story, our mission, and the people behind ${name}.`),
      section("features", "Our values", "", ["Integrity", "Craft", "Community"]),
      section("team", "Meet the team", "", ["Founder & CEO", "Head of Design", "Lead Engineer", "Customer Success"]),
      section("cta", "Want to work with us?", "", [], "Contact us"),
      footer,
    ],
  };

  const services: Page = isShop
    ? {
        name: "Shop",
        slug: "/shop",
        sections: [
          nav,
          section("hero", "Shop the collection", "Hand-picked products, delivered fast."),
          section("gallery", "Best sellers", "", ["Product one", "Product two", "Product three", "Product four", "Product five", "Product six"]),
          section("faq", "Shipping & returns", "", ["How long does delivery take?", "Can I return an item?", "Do you ship internationally?"]),
          footer,
        ],
      }
    : {
        name: isAgency ? "Work" : "Services",
        slug: isAgency ? "/work" : "/services",
        sections: [
          nav,
          section("hero", isAgency ? "Selected work" : "What we offer", "Solutions tailored to your goals."),
          section(isAgency ? "gallery" : "features", isAgency ? "Case studies" : "Our services", "", ["Strategy", "Design", "Development", "Growth"]),
          section("testimonials", "Client stories", "", ["“They delivered beyond expectations.” — Thandi N."]),
          section("cta", "Let's build something together", "", [], "Book a call"),
          footer,
        ],
      };

  const pricing: Page = {
    name: "Pricing",
    slug: "/pricing",
    sections: [
      nav,
      section("hero", "Simple, transparent pricing", "Pick the plan that fits you. Cancel anytime."),
      section("pricing", "Plans", "", ["Basic — R199/mo", "Pro — R499/mo", "Business — R999/mo"], "Choose plan"),
      section("faq", "Frequently asked questions", "", ["Can I change plans later?", "Is there a free trial?", "What payment methods do you accept?"]),
      footer,
    ],
  };

  const contact: Page = {
    name: "Contact",
    slug: "/contact",
    sections: [
      nav,
      section("contact", "Get in touch", "We usually reply within one business day.", ["Name", "Email", "Message"], "Send message"),
      section("faq", "Before you reach out", "", ["Where are you based?", "What are your hours?"]),
      footer,
    ],
  };

  const blog: Page = {
    name: "Blog",
    slug: "/blog",
    sections: [
      nav,
      section("hero", "Insights & news", `Ideas and updates from the ${name} team.`),
      section("blog", "Latest posts", "", ["Getting started guide", "5 tips to grow faster", "Behind the scenes"]),
      section("cta", "Subscribe to our newsletter", "", [], "Subscribe"),
      footer,
    ],
  };

  const pages = [home, about, services, pricing, contact, blog].slice(0, Math.max(1, Math.min(6, pageCount)));

  return {
    siteName: name,
    tagline: `Everything you need for ${topic}.`,
    pages,
    style: {
      ...palette,
      headingFont: isAgency ? "Space Grotesk" : "Poppins",
      bodyFont: "Inter",
      radius: isAgency ? "none" : "large",
    },
  };
}

function hash(s: string) {
  let h = 0;
  for (let i = 0; i < s.length; i++) h = (h * 31 + s.charCodeAt(i)) | 0;
  return h;
}
