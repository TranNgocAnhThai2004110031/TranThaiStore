var prevScrollpos = window.pageYOffset;
window.onscroll = function () {
    var currentScrollPos = window.pageYOffset;
    if (prevScrollpos > currentScrollPos) {
        document.getElementById("header-cover").style.top = "0";
    } else {
        document.getElementById("header-cover").style.top = "-84px";
    }
    prevScrollpos = currentScrollPos;
}
// function showModal(productId) {
//     var modal = document.getElementById("modal" + productId);
//     modal.classList.add("d-block");
// }

// function hideModal(productId) {
//     var modal = document.getElementById("modal" + productId);
//     modal.classList.remove("d-block");
// }
