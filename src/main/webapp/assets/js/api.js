async function apiPost(url, formData) {
    const params = new URLSearchParams();
    for (let [key, value] of formData.entries()) {
        params.append(key, value);
    }
    const res = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString()
    });
    return await res.json();
}

async function apiGet(url) {
    const res = await fetch(url);
    return await res.json();
}