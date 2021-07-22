/* function(): search all DOM elements child of selector and with the requested innerText */
function contains(selector, text) {
  var elements = document.querySelectorAll(selector);
  return [].filter.call(elements, function(element){
    return RegExp(text).test(element.textContent);
  });
}

/* function(): complete the HTML form */
function completeForm(data){
    var map = {
            name: {
                id: '1897',
                type: 'text'
            },
            rut: {
                id: '1898',
                type: 'text'
            },
            code: {
                id: '3000',
                type: 'text'
            },
            age: {
                id: '2529',
                type: 'text'
            },
            region: {
                container: 'regiones_1899_chosen',
                option: '.active-result',
                select: 'regiones_1899',
                type: 'select'
            },
            comuna: {
                container: 'comunas_1899_chosen',
                option: '.active-result',
                select: 'comunas_1899',
                type: 'select',
                wait: 1000
            },
            address: {
                id: '1900',
                type: 'text'
            },
            trayecto: {
                id: 'Ida - Regreso',
                type: 'check'
            },
            destino: {
                id: '1905',
                type: 'text'
            },
            copia: {
                id: 'Si',
                type: 'check'
            },
            declaracion: {
                id: 'en_caso_de_comprobarse_falsedad_en_la_declaracion_de_la_causal_invocada_para_requerir_el_presente_documento_se_incurrira_en_las_penas_del_art_210_del_codigo_penal',
                type: 'check'
            },
            email: {
                id: '2404',
                type: 'text'
            }
        },
        problem = false;

    for (const key in map) {
        if (problem) break;
        var evaluate = function () {problem = true};
        if (map[key].type == 'text') {
            var element = map[key].id ? document.getElementById(map[key].id) : (map[key].query ? document.querySelectorAll(map[key].query) : []);
            if (element) {
                if (!(element instanceof HTMLElement)) element = element[0];
                evaluate = function () { element.value = data[key] };
            }
        } else if (map[key].type == 'select') {
            var container = document.getElementById(map[key].container),
                select = document.getElementById(map[key].select);

            if (!container && select) {
                evaluate = function () {
                    select.value = data[key];
                    console.log(data[key]);
                    select.dispatchEvent(new Event("change"));
                };
            } else if (parent) {
                evaluate = function () {
                    container.dispatchEvent(new MouseEvent('mousedown', {bubbles: true}));
                    var option = contains(map[key].option, data[key]);
                    if (option.length) {
                        option[0].dispatchEvent(new MouseEvent('mouseup', {bubbles: true}));
                        container.dispatchEvent(new MouseEvent('mouseup', {bubbles: true}));
                    } else {
                        problem = true;
                    }
                };
            }
        } else if (map[key].type == 'check') {
            var element = map[key].id ? document.getElementById(map[key].id) : (map[key].query ? document.querySelectorAll(map[key].query) : []);
            if (element) {
                if (!(element instanceof HTMLElement)) element = element[0];
                evaluate = function () { element.checked = true };
            }
        }
        if (map[key].wait) setTimeout(evaluate, map[key].wait);
        else evaluate();
    }

    console.log(problem ? "Error" : "Success");
}

/* function(): hide all DOM elements excepts the captcha */
function hideNoCaptcha(){
  var captchaClass = '.g-recaptcha';
  var el = document.querySelector(captchaClass);
  var node, nodes = [];
  el.style.margin = "auto";
  el.style.width = "fit-content";
  do {
    var parent = el.parentNode;
    for (var i=0, iLen=parent.childNodes.length; i<iLen; i++) {
      node = parent.childNodes[i];
      if (node.nodeType == 1 && node != el) {
        nodes.push(node);
      }
    }
    el = parent;
  } while (el.tagName.toLowerCase() != 'body');
  nodes.forEach(function(node){
    node.style.display = 'none';
  });
};

/* function(): waits for user to check the captcha and click next button */
function check() {
    if(!(grecaptcha && grecaptcha.getResponse().length !== 0)) {
        setTimeout(check, 50);
        return;
    }
    document.getElementsByTagName('button')[1].click();
}

/* function(): execute the automatization */
async function main(data) {
    await completeForm(data);
    if (true) await hideNoCaptcha();
    if (true) check();
}


