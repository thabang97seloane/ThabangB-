import { NextResponse } from "next/server";
import Stripe from "stripe";

export const runtime = "nodejs";

const PRICE_ENV: Record<string, string | undefined> = {
  starter: process.env.STRIPE_PRICE_STARTER,
  pro: process.env.STRIPE_PRICE_PRO,
  team: process.env.STRIPE_PRICE_TEAM,
};

export async function POST(req: Request) {
  const { plan } = (await req.json().catch(() => ({}))) as { plan?: string };
  const price = plan ? PRICE_ENV[plan] : undefined;

  if (!process.env.STRIPE_SECRET_KEY || !price) {
    return NextResponse.json(
      { error: "Payments are not set up yet. Add STRIPE_SECRET_KEY and the STRIPE_PRICE_* variables." },
      { status: 503 },
    );
  }

  const stripe = new Stripe(process.env.STRIPE_SECRET_KEY);
  const site = process.env.NEXT_PUBLIC_SITE_URL || new URL(req.url).origin;

  const session = await stripe.checkout.sessions.create({
    mode: "subscription",
    line_items: [{ price, quantity: 1 }],
    allow_promotion_codes: true,
    success_url: `${site}/app/manage-account/plan?status=success&plan=${plan}`,
    cancel_url: `${site}/app/manage-account/plan?status=cancelled`,
  });

  return NextResponse.json({ url: session.url });
}
