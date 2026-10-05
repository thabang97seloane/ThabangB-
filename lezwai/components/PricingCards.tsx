"use client";

import Link from "next/link";
import { useState } from "react";
import { PLANS, type PlanId } from "@/lib/plans";

export function PricingCards({ currentPlan }: { currentPlan?: PlanId }) {
  const [yearly, setYearly] = useState(false);
  const [loading, setLoading] = useState<PlanId | null>(null);
  const [error, setError] = useState("");

  async function choose(plan: PlanId) {
    setError("");
    setLoading(plan);
    try {
      const res = await fetch("/api/checkout", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ plan }),
      });
      const data = await res.json();
      if (data.url) window.location.href = data.url;
      else setError(data.error || "Could not start checkout.");
    } catch {
      setError("Network error — please try again.");
    } finally {
      setLoading(null);
    }
  }

  return (
    <div>
      <div className="mb-8 flex items-center justify-center gap-3 text-sm font-medium">
        <span className={yearly ? "text-muted" : ""}>Monthly</span>
        <button
          role="switch"
          aria-checked={yearly}
          aria-label="Bill yearly"
          onClick={() => setYearly((y) => !y)}
          className={`relative h-6 w-11 rounded-full transition ${yearly ? "bg-violet" : "bg-line"}`}
        >
          <span className={`absolute top-0.5 h-5 w-5 rounded-full bg-white shadow transition ${yearly ? "left-5.5" : "left-0.5"}`} />
        </button>
        <span className={yearly ? "" : "text-muted"}>
          Yearly <span className="rounded-full bg-lime px-2 py-0.5 text-xs text-ink">Save 20%</span>
        </span>
      </div>

      {error && <p className="mx-auto mb-6 max-w-xl rounded-lg bg-red-50 p-3 text-center text-sm text-red-700">{error}</p>}

      <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
        {PLANS.map((p) => {
          const price = yearly ? Math.round(p.monthly * 0.8) : p.monthly;
          const isCurrent = currentPlan === p.id;
          return (
            <div
              key={p.id}
              className={`relative flex flex-col rounded-2xl border p-6 ${
                p.highlight ? "border-ink bg-ink text-white shadow-xl" : "border-line bg-white"
              }`}
            >
              {p.highlight && (
                <span className="absolute -top-3 left-6 rounded-full bg-lime px-3 py-1 text-xs font-bold text-ink">Most popular</span>
              )}
              <h3 className="font-display text-xl font-bold">{p.name}</h3>
              <p className={`mt-1 text-sm ${p.highlight ? "text-white/70" : "text-muted"}`}>{p.blurb}</p>
              <p className="mt-6">
                <span className="font-display text-4xl font-bold">${price}</span>
                <span className={`text-sm ${p.highlight ? "text-white/60" : "text-muted"}`}>/mo</span>
              </p>
              {yearly && p.monthly > 0 && (
                <p className={`text-xs ${p.highlight ? "text-white/60" : "text-muted"}`}>billed ${price * 12} yearly</p>
              )}
              <ul className="mt-6 flex-1 space-y-2.5 text-sm">
                {p.features.map((f) => (
                  <li key={f} className="flex gap-2">
                    <span className={p.highlight ? "text-lime" : "text-violet"}>✓</span>
                    {f}
                  </li>
                ))}
              </ul>
              {isCurrent ? (
                <span className="mt-6 rounded-full border border-current py-2.5 text-center text-sm font-semibold opacity-70">
                  Current plan
                </span>
              ) : p.id === "free" ? (
                <Link
                  href="/app"
                  className="mt-6 rounded-full border border-ink py-2.5 text-center text-sm font-semibold hover:bg-ink hover:text-white"
                >
                  Start free
                </Link>
              ) : (
                <button
                  onClick={() => choose(p.id)}
                  disabled={loading !== null}
                  className={`mt-6 rounded-full py-2.5 text-sm font-semibold transition disabled:opacity-60 ${
                    p.highlight ? "bg-lime text-ink hover:brightness-95" : "bg-ink text-white hover:bg-ink-soft"
                  }`}
                >
                  {loading === p.id ? "Redirecting…" : currentPlan ? `Switch to ${p.name}` : `Get ${p.name}`}
                </button>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}
