import Anthropic from "@anthropic-ai/sdk";
import { betaZodOutputFormat } from "@anthropic-ai/sdk/helpers/beta/zod";
import { NextResponse } from "next/server";
import { demoSite } from "@/lib/demo";
import { SECTION_TYPES, SiteSchema } from "@/lib/schema";

export const runtime = "nodejs";
export const maxDuration = 300;

const SYSTEM = `You are LEZWAI, an expert website strategist and UX designer.
Given a description of a business or project, plan a complete marketing website.

Rules:
- Return a sitemap of pages. Every page starts with a "navbar" section and ends with a "footer" section.
- Use only these section types: ${SECTION_TYPES.join(", ")}.
- Write real, specific copy for this business (headings, short body text, list items, CTA labels). No lorem ipsum.
- "items" holds list content for the section (nav links, features, testimonials as "“quote” — Name", plan names with prices, FAQ questions, form fields, etc.). Use [] when not needed. "cta" is a button label or "".
- Slugs start with "/" and the home page slug is "/".
- Choose a style guide that suits the brand: hex colours with good contrast between text and background, and Google Fonts names for headingFont and bodyFont.`;

export async function POST(req: Request) {
  let prompt = "";
  let pageCount = 5;
  try {
    const body = await req.json();
    prompt = String(body.prompt ?? "").slice(0, 4000).trim();
    pageCount = Math.max(1, Math.min(12, Number(body.pageCount) || 5));
  } catch {
    return NextResponse.json({ error: "Invalid request body" }, { status: 400 });
  }
  if (!prompt) {
    return NextResponse.json({ error: "Describe your website first." }, { status: 400 });
  }

  if (!process.env.ANTHROPIC_API_KEY) {
    return NextResponse.json({ site: demoSite(prompt, pageCount), mode: "demo" });
  }

  const client = new Anthropic();
  try {
    const response = await client.beta.messages.parse({
      model: "claude-opus-5-5",
      max_tokens: 16000,
      // If a safety classifier declines, the API retries on a fallback model automatically.
      betas: ["server-side-fallback-2026-07-01"],
      fallbacks: "default",
      output_config: { effort: "medium", format: betaZodOutputFormat(SiteSchema) },
      system: SYSTEM,
      messages: [
        {
          role: "user",
          content: `Plan a website with about ${pageCount} pages for:\n\n${prompt}`,
        },
      ],
    });

    if (response.stop_reason === "refusal") {
      return NextResponse.json({ error: "The AI declined this request. Try rephrasing your description." }, { status: 422 });
    }
    if (response.stop_reason === "max_tokens" || !response.parsed_output) {
      return NextResponse.json({ error: "The AI response was incomplete. Try fewer pages." }, { status: 502 });
    }
    return NextResponse.json({ site: response.parsed_output, mode: "ai" });
  } catch (err) {
    if (err instanceof Anthropic.AuthenticationError) {
      return NextResponse.json({ error: "Your ANTHROPIC_API_KEY is invalid." }, { status: 500 });
    }
    if (err instanceof Anthropic.RateLimitError) {
      return NextResponse.json({ error: "Too many requests right now — please try again in a minute." }, { status: 429 });
    }
    if (err instanceof Anthropic.APIError) {
      console.error("Anthropic API error", err.status, err.message);
      return NextResponse.json({ error: "AI service error. Please try again." }, { status: 502 });
    }
    console.error(err);
    return NextResponse.json({ error: "Something went wrong." }, { status: 500 });
  }
}
