import Link from "next/link";
import { Logo } from "./Logo";

const COLS = [
  { title: "Product", links: [["Builder", "/app"], ["Pricing", "/pricing"], ["Features", "/#features"]] },
  { title: "Account", links: [["My plan", "/app/manage-account/plan"], ["Start free", "/app"]] },
  { title: "Resources", links: [["How it works", "/#how"], ["FAQ", "/#faq"]] },
];

export function Footer() {
  return (
    <footer className="bg-ink text-white">
      <div className="mx-auto grid max-w-7xl gap-10 px-4 py-14 sm:px-6 md:grid-cols-[2fr_1fr_1fr_1fr]">
        <div>
          <Logo dark />
          <p className="mt-4 max-w-xs text-sm text-white/60">
            The AI website design tool. Go from idea to sitemap, wireframe and style guide in minutes.
          </p>
        </div>
        {COLS.map((c) => (
          <div key={c.title}>
            <h4 className="text-sm font-semibold">{c.title}</h4>
            <ul className="mt-3 space-y-2">
              {c.links.map(([label, href]) => (
                <li key={label}>
                  <Link href={href} className="text-sm text-white/60 hover:text-white">
                    {label}
                  </Link>
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>
      <div className="border-t border-white/10 py-6 text-center text-xs text-white/50">
        © {new Date().getFullYear()} LEZWAI. All rights reserved.
      </div>
    </footer>
  );
}
