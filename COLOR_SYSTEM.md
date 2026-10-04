# UsmanMart Design System & Color Psychology Specification

This document defines the complete color system, typography scale, component design guidelines, and human-computer interaction (HCI) rules for the **UsmanMart** ecosystem (usable across both Seller and Buyer Android apps).

---

## 1. Brand Color Palette & Psychology

| Color Role | Name | HEX Code | Psychological Intent & Usage |
| :--- | :--- | :--- | :--- |
| **Primary** | Terracotta Coral | `#E2654A` | Represents warmth, craftsmanship, energy, and commerce vitality. Used for primary CTA buttons, active navigation items, key conversion highlights. |
| **Primary Dark** | Deep Terracotta | `#C44F37` | Conveys richness and visual contrast. Used for header gradients, pressed button states, and primary title text. |
| **Primary Light** | Soft Sand Tint | `#FBE7E0` | Calming background tint for stat icons, feature chips, and active highlights. |
| **Secondary** | Deep Slate Navy | `#1E293B` | Conveys trust, security, and structure. Used for secondary titles, navigation accents, and monetary figures. |
| **Canvas Background** | Natural Off-White | `#FAF7F5` | Reduces eye strain compared to harsh `#FFFFFF` backgrounds and makes product photos pop. |
| **Card Surface** | Pure White | `#FFFFFF` | Used for elevated cards, modal dialogs, and clean content containers. |
| **Border / Divider** | Warm Warm Grey | `#F0E6DE` | Subtle stroke color for card borders and line dividers. |

---

## 2. Text Hierarchy & Typography Tokens

| Token Name | HEX Code | Description & Usage |
| :--- | :--- | :--- |
| `text_primary` | `#2D241F` | Charcoal warmth for maximum readability on headers and body text. |
| `text_secondary` | `#7A6B63` | Muted warm slate for subtitles, metadata, and timestamps. |
| `text_tertiary` | `#A09088` | Soft placeholder/hint text. |
| `text_on_primary` | `#FFFFFF` | Crisp white text on primary terracotta backgrounds. |

---

## 3. Status & Functional Color Palette

| Status State | Background HEX | Text HEX | Meaning |
| :--- | :--- | :--- | :--- |
| **Pending / In Progress** | `#FFF3E0` | `#D97706` | Amber warmth indicating waiting/processing. |
| **Completed / Shipped** | `#E3F6E8` | `#059669` | Emerald growth indicating success, delivered, or completed. |
| **Out of Stock / Danger** | `#FEE2E2` | `#DC2626` | Crimson alert for low inventory, errors, or cancellations. |

---

## 4. Reusable XML Resource Palette (`res/values/colors.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- Brand Primary Tokens -->
    <color name="terracotta_primary">#E2654A</color>
    <color name="terracotta_dark">#C44F37</color>
    <color name="terracotta_light">#FBE7E0</color>
    
    <!-- Secondary & Neutral Tokens -->
    <color name="slate_navy">#1E293B</color>
    <color name="dashboard_bg">#FAF7F5</color>
    <color name="card_white">#FFFFFF</color>
    <color name="divider_light">#F0E6DE</color>

    <!-- Typography Tokens -->
    <color name="text_primary">#2D241F</color>
    <color name="text_secondary">#7A6B63</color>
    <color name="text_hint">#A09088</color>
    
    <!-- Functional Status Tokens -->
    <color name="status_pending_bg">#FFF3E0</color>
    <color name="status_pending_text">#D97706</color>
    <color name="status_completed_bg">#E3F6E8</color>
    <color name="status_completed_text">#059669</color>
    <color name="status_error_bg">#FEE2E2</color>
    <color name="status_error_text">#DC2626</color>
</resources>
```

---

## 5. UI/UX & HCI Guidelines for Buyer App Implementation

1. **Card Corner Radius**: Use `16dp` for item cards and `20dp` for major header/profile cards.
2. **Elevation**: Prefer subtle borders (`app:strokeWidth="1dp"` with `@color/divider_light`) over harsh elevations (`app:cardElevation="2dp"` to `4dp`).
3. **Buttons**: Corner radius `14dp` to `16dp`, minimum touch height `48dp` to `54dp` for primary action buttons with touch ripples (`?attr/selectableItemBackground`).
4. **Input Fields**: Soft rounded background containers (`@drawable/bg_input_field`) with `14dp` padding and clear hint contrast.
