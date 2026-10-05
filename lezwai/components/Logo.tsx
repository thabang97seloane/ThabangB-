import Link from "next/link";

export function Logo({ dark = false }: { dark?: boolean }) {
  return (
    <Link href="/" className="flex items-center gap-2" aria-label="LEZWAI home">
      <span className="grid h-8 w-8 place-items-center rounded-lg bg-lime text-ink">
        <svg viewBox="0 0 24 24" className="h-5 w-5" fill="none" stroke="currentColor" strokeWidth="2.5" aria-hidden>
          <path d="M5 4v16h14" strokeLinecap="round" strokeLinejoin="round" />
          <path d="M10 14l4-8 4 8" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      </span>
      <span className={`font-display text-xl font-bold tracking-tight ${dark ? "text-white" : "text-ink"}`}>LEZWAI</span>
    </Link>
  );
}
