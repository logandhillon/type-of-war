const config = {
  windows: {
    label: "Windows (.exe)",
    icon: "ph-fill ph-windows-logo",
    href: "https://github.com/logandhillon/type-of-war/releases/latest"
  },
  mac: {
    label: "macOS (.dmg)",
    icon: "ph-fill ph-apple-logo",
    href: "https://github.com/logandhillon/type-of-war/releases/latest"
  },
  linux: {
    label: "Linux (.deb)",
    icon: "ph-fill ph-linux-logo",
    href: "https://github.com/logandhillon/type-of-war/releases/latest"
  },
  other: {
    label: "Universal (.jar)",
    icon: "ph-fill ph-coffee",
    href: "https://github.com/logandhillon/type-of-war/releases/latest"
  }
};

function onLoad() {
  document.getElementById("copyright").textContent =
    `© 2025-${new Date().getFullYear()} logandhillon.com. All rights reserved.`;

  const platform = navigator.platform.toLowerCase();

  let os = "windows";
  if (platform.includes("mac")) os = "mac";
  else if (platform.includes("linux")) os = "linux";

  const primary = config[os];

  const labelEl = document.getElementById("primary-label");
  const iconEl = document.getElementById("primary-icon");
  const linkEl = document.getElementById("primary-download");
  const dropdown = document.getElementById("download-dropdown");

  // primary button
  labelEl.textContent = primary.label;
  iconEl.className = "ph ph-download-simple text-lg";
  linkEl.href = primary.href;
  linkEl.setAttribute("target", "_blank")

  // clear dropdown
  dropdown.innerHTML = "";

  // populate dropdown with non-primary platforms
  Object.entries(config).forEach(([key, value]) => {
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

document.addEventListener("DOMContentLoaded", onLoad);
