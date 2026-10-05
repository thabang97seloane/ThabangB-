"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

export function HeroPrompt() {
  const router = useRouter();
  const [value, setValue] = useState("");
  return (
    <form
      onSubmit={(e) => {
        e.preventDefault();
        router.push(value.trim() ? `/app?prompt=${encodeURIComponent(value.trim())}` : "/app");
      }}
      className="mx-auto mt-10 flex max-w-2xl flex-col gap-2 rounded-2xl border border-line bg-white p-2 shadow-lg sm:flex-row"
    >
      <label htmlFor="hero-prompt" className="sr-only">Describe your website</label>
      <input
        id="hero-prompt"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        placeholder="Describe a website… e.g. a bakery in Durban with online orders"
        className="min-w-0 flex-1 rounded-xl px-4 py-3 outline-none"
      />
      <button className="rounded-xl bg-violet px-5 py-3 font-semibold text-white hover:brightness-110">✦ Generate</button>
    </form>
  );
}
