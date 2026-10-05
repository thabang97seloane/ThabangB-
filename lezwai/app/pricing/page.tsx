import type { Metadata } from "next";
import { Footer } from "@/components/Footer";
import { Navbar } from "@/components/Navbar";
import { PricingCards } from "@/components/PricingCards";

export const metadata: Metadata = { title: "Pricing — LEZWAI" };

export default function PricingPage() {
  return (
    <>
      <Navbar />
      <main className="mx-auto max-w-7xl px-4 py-20 sm:px-6">
        <h1 className="text-center font-display text-5xl font-bold tracking-tight">Pricing</h1>
        <p className="mt-3 mb-12 text-center text-muted">Plans for solo makers, studios and agencies. Cancel anytime.</p>
        <PricingCards />
      </main>
      <Footer />
    </>
  );
}
