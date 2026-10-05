import { z } from "zod";

export const SECTION_TYPES = [
  "navbar",
  "hero",
  "logos",
  "features",
  "stats",
  "gallery",
  "testimonials",
  "pricing",
  "team",
  "blog",
  "faq",
  "contact",
  "cta",
  "footer",
] as const;

export const SectionSchema = z.object({
  type: z.enum(SECTION_TYPES),
  heading: z.string(),
  body: z.string(),
  items: z.array(z.string()),
  cta: z.string(),
});

export const PageSchema = z.object({
  name: z.string(),
  slug: z.string(),
  sections: z.array(SectionSchema),
});

export const StyleSchema = z.object({
  primary: z.string(),
  secondary: z.string(),
  background: z.string(),
  text: z.string(),
  headingFont: z.string(),
  bodyFont: z.string(),
  radius: z.enum(["none", "small", "large"]),
});

export const SiteSchema = z.object({
  siteName: z.string(),
  tagline: z.string(),
  pages: z.array(PageSchema),
  style: StyleSchema,
});

export type SectionType = (typeof SECTION_TYPES)[number];
export type Section = z.infer<typeof SectionSchema>;
export type Page = z.infer<typeof PageSchema>;
export type Style = z.infer<typeof StyleSchema>;
export type Site = z.infer<typeof SiteSchema>;

export const FONT_OPTIONS = [
  "Inter",
  "Poppins",
  "Playfair Display",
  "Space Grotesk",
  "DM Sans",
  "Lora",
  "Montserrat",
  "Outfit",
];
