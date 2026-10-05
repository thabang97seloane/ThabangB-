import type { CSSProperties } from "react";
import type { Section, Style } from "@/lib/schema";

const RADIUS = { none: "0px", small: "6px", large: "16px" } as const;

type Props = { section: Section; style?: Style; styled?: boolean };

// Renders one section either as a grey wireframe or with the style guide applied.
export function SectionBlock({ section: s, style, styled = false }: Props) {
  const on = styled && style;
  const r = on ? RADIUS[style.radius] : "8px";
  const root: CSSProperties = on
    ? { background: style.background, color: style.text, fontFamily: `'${style.bodyFont}', sans-serif` }
    : {};
  const h: CSSProperties = on ? { fontFamily: `'${style.headingFont}', sans-serif` } : {};
  const btn: CSSProperties = on
    ? { background: style.primary, color: "#fff", borderRadius: r }
    : { borderRadius: r };
  const card: CSSProperties = { borderRadius: r };
  const box = on ? "" : "wf-box";
  const boxStyle: CSSProperties = on
    ? { borderRadius: r, background: `linear-gradient(135deg, ${style.primary}22, ${style.secondary}33)` }
    : { borderRadius: r };
  const btnCls = on ? "px-4 py-2 text-sm font-semibold" : "bg-neutral-800 px-4 py-2 text-sm font-semibold text-white";
  const muted = "opacity-70";
  const items = s.items.length ? s.items : ["Item one", "Item two", "Item three"];

  const Button = ({ label }: { label: string }) => (label ? <span className={`inline-block ${btnCls}`} style={btn}>{label}</span> : null);

  let content: React.ReactNode;
  switch (s.type) {
    case "navbar":
      content = (
        <div className="flex flex-wrap items-center justify-between gap-3 py-4">
          <span className="font-bold" style={h}>{s.heading}</span>
          <div className="hidden gap-5 text-sm sm:flex">
            {s.items.map((i) => <span key={i} className={muted}>{i}</span>)}
          </div>
          <Button label={s.cta} />
        </div>
      );
      break;
    case "hero":
      content = (
        <div className="grid items-center gap-8 py-14 md:grid-cols-2">
          <div>
            <h2 className="text-3xl font-bold leading-tight sm:text-4xl" style={h}>{s.heading}</h2>
            {s.body && <p className={`mt-4 ${muted}`}>{s.body}</p>}
            <div className="mt-6"><Button label={s.cta || "Get started"} /></div>
          </div>
          <div className={`aspect-[4/3] ${box}`} style={boxStyle} />
        </div>
      );
      break;
    case "logos":
      content = (
        <div className="py-8 text-center">
          <p className={`text-sm ${muted}`}>{s.heading}</p>
          <div className="mt-4 flex flex-wrap justify-center gap-6 font-bold opacity-50">
            {items.map((i) => <span key={i}>{i}</span>)}
          </div>
        </div>
      );
      break;
    case "stats":
      content = (
        <div className="py-12">
          <h3 className="text-center text-2xl font-bold" style={h}>{s.heading}</h3>
          <div className="mt-6 grid gap-4 sm:grid-cols-3">
            {items.map((i) => {
              const [num, ...rest] = i.split(" ");
              return (
                <div key={i} className="border p-5 text-center" style={{ ...card, borderColor: on ? `${style.text}22` : "#e5e5e5" }}>
                  <div className="text-3xl font-bold" style={on ? { ...h, color: style.primary } : h}>{num}</div>
                  <div className={`text-sm ${muted}`}>{rest.join(" ")}</div>
                </div>
              );
            })}
          </div>
        </div>
      );
      break;
    case "features":
    case "team":
    case "blog":
    case "gallery":
      content = (
        <div className="py-12">
          <h3 className="text-center text-2xl font-bold" style={h}>{s.heading}</h3>
          {s.body && <p className={`mx-auto mt-2 max-w-xl text-center ${muted}`}>{s.body}</p>}
          <div className={`mt-8 grid gap-5 ${s.type === "gallery" ? "grid-cols-2 sm:grid-cols-3" : "sm:grid-cols-3"}`}>
            {items.map((i) => (
              <div key={i}>
                <div className={`${s.type === "team" ? "mx-auto aspect-square w-24 !rounded-full" : "aspect-video"} ${box}`} style={boxStyle} />
                <p className={`mt-3 font-semibold ${s.type === "team" ? "text-center" : ""}`} style={h}>{i}</p>
                {s.type !== "gallery" && s.type !== "team" && <p className={`text-sm ${muted}`}>Short supporting description.</p>}
              </div>
            ))}
          </div>
        </div>
      );
      break;
    case "testimonials":
      content = (
        <div className="py-12">
          <h3 className="text-center text-2xl font-bold" style={h}>{s.heading}</h3>
          <div className="mt-8 grid gap-5 sm:grid-cols-2">
            {items.map((i) => (
              <blockquote key={i} className="border p-5 italic" style={{ ...card, borderColor: on ? `${style.text}22` : "#e5e5e5" }}>{i}</blockquote>
            ))}
          </div>
        </div>
      );
      break;
    case "pricing":
      content = (
        <div className="py-12">
          <h3 className="text-center text-2xl font-bold" style={h}>{s.heading}</h3>
          <div className="mt-8 grid gap-5 sm:grid-cols-3">
            {items.map((i, idx) => (
              <div
                key={i}
                className="border p-6 text-center"
                style={{ ...card, borderColor: on && idx === 1 ? style.primary : on ? `${style.text}22` : "#d4d4d4", borderWidth: idx === 1 ? 2 : 1 }}
              >
                <p className="font-semibold" style={h}>{i}</p>
                <div className={`mx-auto my-4 h-16 ${box}`} style={boxStyle} />
                <Button label={s.cta || "Choose"} />
              </div>
            ))}
          </div>
        </div>
      );
      break;
    case "faq":
      content = (
        <div className="mx-auto max-w-2xl py-12">
          <h3 className="text-center text-2xl font-bold" style={h}>{s.heading}</h3>
          <div className="mt-6 divide-y" style={{ borderColor: on ? `${style.text}22` : undefined }}>
            {items.map((i) => (
              <div key={i} className="flex justify-between py-3 font-medium"><span>{i}</span><span>+</span></div>
            ))}
          </div>
        </div>
      );
      break;
    case "contact":
      content = (
        <div className="grid gap-8 py-12 md:grid-cols-2">
          <div>
            <h3 className="text-2xl font-bold" style={h}>{s.heading}</h3>
            {s.body && <p className={`mt-2 ${muted}`}>{s.body}</p>}
          </div>
          <div className="space-y-3">
            {items.map((i) => (
              <div key={i} className="border px-3 py-2 text-sm opacity-70" style={{ ...card, borderColor: on ? `${style.text}33` : "#d4d4d4" }}>{i}</div>
            ))}
            <Button label={s.cta || "Submit"} />
          </div>
        </div>
      );
      break;
    case "cta":
      content = (
        <div
          className="my-8 px-6 py-12 text-center"
          style={on ? { ...card, background: style.primary, color: "#fff" } : { ...card, background: "#262626", color: "#fff" }}
        >
          <h3 className="text-2xl font-bold" style={h}>{s.heading}</h3>
          {s.body && <p className="mt-2 opacity-80">{s.body}</p>}
          {s.cta && (
            <span
              className="mt-5 inline-block px-4 py-2 text-sm font-semibold"
              style={{ borderRadius: r, background: on ? style.secondary : "#fff", color: on ? style.text : "#111" }}
            >
              {s.cta}
            </span>
          )}
        </div>
      );
      break;
    case "footer":
      content = (
        <div className="flex flex-wrap items-center justify-between gap-3 border-t py-6 text-sm" style={{ borderColor: on ? `${style.text}22` : undefined }}>
          <span className="font-bold" style={h}>{s.heading}</span>
          <div className={`flex gap-4 ${muted}`}>{s.items.map((i) => <span key={i}>{i}</span>)}</div>
          {s.body && <span className={`w-full text-xs ${muted}`}>{s.body}</span>}
        </div>
      );
      break;
  }

  return (
    <div className={`px-6 ${on ? "" : "bg-white text-neutral-800"}`} style={root}>
      {content}
    </div>
  );
}
