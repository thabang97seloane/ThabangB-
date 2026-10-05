export type PlanId = "free" | "starter" | "pro" | "team";

export type Plan = {
  id: PlanId;
  name: string;
  monthly: number;
  blurb: string;
  features: string[];
  highlight?: boolean;
};

// Prices are in USD. The matching Stripe Price IDs live in env vars
// (STRIPE_PRICE_STARTER, STRIPE_PRICE_PRO, STRIPE_PRICE_TEAM).
export const PLANS: Plan[] = [
  {
    id: "free",
    name: "Free",
    monthly: 0,
    blurb: "Try LEZWAI and plan your first site.",
    features: ["1 project", "AI sitemap (up to 3 pages)", "Wireframe preview", "JSON export"],
  },
  {
    id: "starter",
    name: "Starter",
    monthly: 19,
    blurb: "For freelancers shipping client sites.",
    features: ["10 projects", "Up to 10 pages per site", "Style guide editor", "HTML export"],
  },
  {
    id: "pro",
    name: "Pro",
    monthly: 39,
    blurb: "For designers who build every week.",
    features: ["Unlimited projects", "Unlimited pages", "Priority AI generation", "HTML + JSON export", "Remove LEZWAI branding"],
    highlight: true,
  },
  {
    id: "team",
    name: "Team",
    monthly: 79,
    blurb: "For agencies collaborating on sites.",
    features: ["Everything in Pro", "5 team seats", "Shared component library", "Priority support"],
  },
];
