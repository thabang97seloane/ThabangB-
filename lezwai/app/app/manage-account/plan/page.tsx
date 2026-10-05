import type { Metadata } from "next";
import Link from "next/link";
import { Logo } from "@/components/Logo";
import { PricingCards } from "@/components/PricingCards";
import { PLANS, type PlanId } from "@/lib/plans";

export const metadata: Metadata = { title: "Plan & billing — LEZWAI" };

export default async function PlanPage({ searchParams }: { searchParams: Promise<{ status?: string; plan?: string }> }) {
  const { status, plan } = await searchParams;
  // Until accounts are connected (see README), everyone is shown on the Free plan,
  // except right after a successful Stripe checkout.
  const current: PlanId = status === "success" && PLANS.some((p) => p.id === plan) ? (plan as PlanId) : "free";
  const currentPlan = PLANS.find((p) => p.id === current)!;

  return (
    <div className="min-h-screen bg-paper">
      <header className="flex h-16 items-center justify-between border-b border-line px-4 sm:px-6">
        <Logo />
        <Link href="/app" className="rounded-full bg-ink px-4 py-2 text-sm font-semibold text-white">Open builder</Link>
      </header>
      <div className="mx-auto flex max-w-7xl flex-col gap-8 px-4 py-10 sm:px-6 lg:flex-row">
        <aside className="lg:w-56 lg:shrink-0">
          <p className="text-xs font-semibold uppercase tracking-wider text-muted">Manage account</p>
          <nav className="mt-3 flex gap-1 lg:flex-col">
            <span className="rounded-md px-3 py-2 text-sm text-muted">Profile</span>
            <span className="rounded-md bg-violet-soft px-3 py-2 text-sm font-semibold text-violet">Plan & billing</span>
            <span className="rounded-md px-3 py-2 text-sm text-muted">Team</span>
          </nav>
        </aside>
        <main className="flex-1">
          <h1 className="font-display text-3xl font-bold">Plan & billing</h1>
          {status === "success" && (
            <p className="mt-4 rounded-lg bg-green-50 p-3 text-sm text-green-800">Payment successful — welcome to {currentPlan.name}! 🎉</p>
          )}
          {status === "cancelled" && (
            <p className="mt-4 rounded-lg bg-yellow-50 p-3 text-sm text-yellow-800">Checkout cancelled. You have not been charged.</p>
          )}
          <div className="mt-6 flex flex-wrap items-center justify-between gap-4 rounded-2xl border border-line bg-white p-6">
            <div>
              <p className="text-sm text-muted">Current plan</p>
              <p className="font-display text-2xl font-bold">{currentPlan.name}</p>
              <p className="text-sm text-muted">{currentPlan.monthly === 0 ? "Free forever" : `$${currentPlan.monthly}/month`}</p>
            </div>
            <div className="text-sm">
              <p className="text-muted">Usage this month</p>
              <p className="font-semibold">1 of {current === "free" ? "1" : "∞"} projects</p>
            </div>
          </div>
          <h2 className="mt-12 mb-6 font-display text-xl font-bold">Change plan</h2>
          <PricingCards currentPlan={current} />
        </main>
      </div>
    </div>
  );
}
