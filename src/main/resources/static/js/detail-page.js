document.addEventListener("DOMContentLoaded", function () {
    const mainImage = document.getElementById("main-product-image");
    const thumbs = document.querySelectorAll(".product-thumb");

    if (!mainImage || thumbs.length === 0) {
        return;
    }

    thumbs.forEach(function (thumb) {
        thumb.addEventListener("click", function () {
            const image = thumb.querySelector("img");
            if (!image) {
                return;
            }

            mainImage.src = image.src;
            mainImage.alt = image.alt;

            thumbs.forEach(function (item) {
                item.classList.remove("is-active", "border-primary", "border-2");
                item.classList.add("border", "border-border");
            });

            thumb.classList.remove("border", "border-border");
            thumb.classList.add("is-active", "border-primary", "border-2");
        });
    });
});
