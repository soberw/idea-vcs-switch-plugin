# README Sync Checklist

Use this checklist whenever `README.md` or `README.zh-CN.md` is changed.

## Trigger Conditions

- A section is added, removed, renamed, or reordered in either README
- Build, run, install, verify, or publish commands change
- Plugin metadata changes:
  - plugin name
  - vendor
  - email
  - source URL
  - versioned artifact path
- Marketplace requirements or release steps change
- Validation steps or expected behavior change

## Section Mapping

Keep these sections aligned between the two files:

| README.md | README.zh-CN.md |
| --- | --- |
| `Current Scope` | `当前范围` |
| `Maintainer` | `维护者` |
| `Build` | `构建` |
| `Compatibility Verification` | `兼容性校验` |
| `Install Into Your Existing IDEA` | `安装到你当前的 IDEA` |
| `Optional Sandbox Run` | `可选的沙盒运行` |
| `Manual Validation Checklist` | `手工验证清单` |
| `Marketplace Publish Prerequisites` | `发布到 Marketplace 前的准备` |
| `Marketplace Publish Commands` | `发布命令` |

If a new section is introduced in one README, add the corresponding section to the other README in the same position.

## Content Rules

- Keep command lines functionally identical across both files.
- Keep file paths, environment variable names, and task names identical across both files.
- Translate explanations, not code, paths, or environment variable names.
- Keep the top navigation links synchronized:
  - `README.md` links to `README.zh-CN.md` and this checklist
  - `README.zh-CN.md` links to `README.md` and this checklist
- If one README mentions an extra warning, note, or prerequisite, copy the same meaning into the other README.

## Review Steps

1. Diff `README.md` and `README.zh-CN.md` side by side.
2. Verify all headings appear in both files and in the same order.
3. Verify all command blocks exist in both files.
4. Verify artifact paths and environment variable names match exactly.
5. Verify maintainer metadata matches exactly.
6. Verify navigation links at the top of both files still work.
7. If only one README changed, explicitly confirm the other README does not need an update.

## Commit Guidance

- If both files changed together, use a commit message that says the README files were synchronized.
- If only one file changed intentionally, mention in the commit or PR description why the other file did not need an update.
