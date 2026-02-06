function populateDownloadBtns(btns) {
  const platform = navigator.platform.toLowerCase();

  let os = "windows";
  if (platform.includes("mac")) os = "mac"; else if (platform.includes("linux")) os = "linux";

  const primary = btns[os];

  const labelEl = document.getElementById("primary-label");
  const iconEl = document.getElementById("primary-icon");
  const linkEl = document.getElementById("primary-download");
  const dropdown = document.getElementById("download-dropdown");

  // primary button
  labelEl.textContent = primary.label;
  iconEl.className = "ph ph-download-simple text-lg";
  linkEl.href = primary.href;

  // clear dropdown
  dropdown.innerHTML = "";

  // populate dropdown with non-primary platforms
  Object.entries(btns).forEach(([key, value]) => {
    if (key === os) return;

    const a = document.createElement("a");
    a.href = value.href;
    a.dataset.os = key;
    a.className = "block px-4 py-2 text-sm text-white hover:bg-gray-900 flex items-center gap-2";
    a.setAttribute("target", "_blank")

    const icon = document.createElement("i");
    icon.className = value.icon;

    const text = document.createTextNode(value.label);

    a.appendChild(icon);
    a.appendChild(text);
    dropdown.appendChild(a);
  });
}

async function getLatestVersion() {
  const releaseRes = await fetch(`https://api.github.com/repos/logandhillon/type-of-war/releases/latest`, {
    headers: {
      "Accept": "application/vnd.github+json",
    },
  });

  if (!releaseRes.ok) throw new Error("Failed to fetch latest release");

  const release = await releaseRes.json();
  return await release.tag_name;
}

function onLoad() {
  document.getElementById("copyright").textContent = `© 2025-${new Date().getFullYear()} logandhillon.com. All rights reserved.`;

  // get latest ver from gh api, then populate dl btns
  getLatestVersion()
    .then(version => populateDownloadBtns({
      windows: {
        label: "Windows (.exe)",
        icon: "ph-fill ph-windows-logo",
        href: `https://github.com/logandhillon/type-of-war/releases/download/${version}/type-of-war-${version}-windows.zip`
      }, mac: {
        label: "macOS (.dmg)",
        icon: "ph-fill ph-apple-logo",
        href: `https://github.com/logandhillon/type-of-war/releases/download/${version}/type-of-war-${version}-macos.zip`
      }, linux: {
        label: "Linux (.deb)",
        icon: "ph-bold ph-linux-logo",
        href: `https://github.com/logandhillon/type-of-war/releases/download/${version}/type-of-war-${version}-linux.zip`
      }, other: {
        label: "Other", icon: "ph ph-code", href: "https://github.com/logandhillon/type-of-war/releases/latest"
      }
    }));
}

document.addEventListener("DOMContentLoaded", onLoad);
