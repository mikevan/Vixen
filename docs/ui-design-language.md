# Vixen UI design language

## Frames with a Back option

Approved by the owner on 2026-11-15, using the deployed build 148 frame as the
visual reference. This is the standard for every Vixen frame that offers Back.

- Form the Back area from the frame itself, in its upper-left corner.
- Continue the frame's textured rails, bevels, thickness, lighting and material
  around the Back area. Preserve the original top and left rails and join the
  inner return continuously to them.
- Place the Back label and its touch target within that integrated area.
- Use the existing header space. Keep content clear of the return without adding
  an outer frame, a separate navigation band or a duplicate lower Back control.
- Do not paste a flat rectangle, independently outlined button or miniature frame
  over the housing. Matching colors alone does not match the frame's design.

The reference implementation is `drawHousingBackReturn` with
`HousingBorderBack` in `OpsAssembly.kt`; `PageBackControl` reserves header space.
The housing renders the artwork and the Back target handles navigation. Apply
this same treatment when adding or revising other frames with a Back option.
This decision establishes the standard; it does not claim every older dialog or
frame has already been migrated.

Review changes on the actual phone for continuous rail joins, matching material
and lighting, readable labels, and unobstructed content before declaring visual
work complete.


## Application coverage — build 149 preview

The common frame renderer now owns Back on the navigation pages, capture decode,
Wireshark, behavior timeline, event details, Hunter card viewer and Hunt view.
Builder's result, load/save menus, Wireshark-check menu and check confirmation,
and the Clear Card confirmation use the same textured border return. Duplicate
bottom Back controls and the standalone Back-button renderer have been removed.

Navigation callbacks retain their original meaning, including returning from a
selected Hunter card to its list and from a Builder finding to the blocks. Other
actions such as Cancel, Close, Restore and Stop keep their own meanings; this
standard does not turn them into Back actions. Build 149 is deployed for owner
review. Commit and push were explicitly authorized on 2026-11-15.
