/* function(): waits x ms*/
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/* function(): get URL from download document button */
async function getURL() {
    while (!document.getElementById("linkpdf").href.includes("tramites")) {
        await sleep(500);
    }
    console.log("DOCUMENT-URL", document.getElementById("linkpdf").href);
}

getURL();