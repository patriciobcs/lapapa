package com.lapapa.app.chatBotForm;

import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_RUT_REGEX =
            Pattern.compile("^(\\d{1,3}(?:\\d{1,3}){2}-[\\dkK])$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_CODE =
            Pattern.compile("A?\\d{9}", Pattern.CASE_INSENSITIVE);

    enum SmartCompareMode {COMPARE_REGION, COMPARE_COMUNA};

    public static final String[] REGIONS = {
            "Tarapacá", "Antofagasta", "Atacama", "Coquimbo", "Valparaíso",
            "Del Libertador Gral. Bernardo O’Higgins", "Del Maule", "Del Biobío",
            "De la Araucanía", "De los Lagos", "Aysén del Gral. Carlos Ibáñez del Campo",
            "Magallanes y de la Antártica Chilena", "Metropolitana de Santiago",
            "De los Ríos", "Arica y Parinacota", "Ñuble"
            };
    public static final String[][] REGIONS_KEYWORDS = {
            {"tarapaca"},
            {"antofagasta"},
            {"atacama"},
            {"coquimbo"},
            {"valparaiso"},
            {"libertador", "bernardo", "ohiggins"},
            {"maule"},
            {"biobio"},
            {"araucania"},
            {"lagos"},
            {"aysen", "carlos", "ibañez", "campo"},
            {"magallanes", "antartica"},
            {"metropolitana", "santiago"},
            {"rios"},
            {"arica", "parinacota"},
            {"ñuble"}
    };

    public static final String[][][] COMUNAS_KEYWORDS = {
            {
                    {"camiña"},
                    {"pozo", "almonte"},
                    {"iquique"},
                    {"alto", "hospicio"},
                    {"colchane"},
                    {"huara"},
                    {"pica"}
            },
            {
                    {"maria", "elena"},
                    {"mejillones"},
                    {"antofagasta"},
                    {"tocopilla"},
                    {"ollague"},
                    {"san", "pedro", "atacama"},
                    {"sierra","gorda"},
                    {"calama"},
                    {"taltal"}
            },
            {
                    {"caldera"},
                    {"diego", "almagro"},
                    {"huasco"},
                    {"copiapo"},
                    {"alto", "carmen"},
                    {"tierra", "amarilla"},
                    {"freirina"},
                    {"chañaral"},
                    {"vallenar"}
            },
            {
                    {"serena"},
                    {"andacollo"},
                    {"paiguano"},
                    {"punitaqui"},
                    {"vilos"},
                    {"salamanca"},
                    {"combarbala"},
                    {"ovalle"},
                    {"coquimbo"},
                    {"monte", "patria"},
                    {"illapel"},
                    {"vicuña"},
                    {"rio","hurtado"},
                    {"higuera"},
                    {"canela"}
            },
            {
                    {"ligua"},
                    {"quillota"},
                    {"calera"},
                    {"villa", "alemana"},
                    {"felipe"},
                    {"catemu"},
                    {"concon"},
                    {"maria"},
                    {"rinconada"},
                    {"limache"},
                    {"esteban"},
                    {"cartagena"},
                    {"algarrobo"},
                    {"viña", "mar"},
                    {"quisco"},
                    {"papudo"},
                    {"nogales"},
                    {"petorca"},
                    {"hijuelas"},
                    {"putaendo"},
                    {"yabo"},
                    {"calle","larga"},
                    {"casablanca"},
                    {"andes"},
                    {"quintero"},
                    {"cruz"},
                    {"llaillay"},
                    {"antonio"},
                    {"quilpue"},
                    {"cabildo"},
                    {"domingo"},
                    {"pascua"},
                    {"valparaiso"},
                    {"panquehue"},
                    {"puchuncavi"},
                    {"zapallar"},
                    {"juan", "fernandez"},
                    {"olmue"}
            },
            {
                    {"fernando"},
                    {"pichidegua"},
                    {"codegua"},
                    {"navidad"},
                    {"cabras"},
                    {"rancagua"},
                    {"vicente"},
                    {"litueche"},
                    {"chepica"},
                    {"malloa"},
                    {"lolol"},
                    {"rengo"},
                    {"chimbarongo"},
                    {"requinoa"},
                    {"cruz"},
                    {"nancagua"},
                    {"mostazal"},
                    {"paredones"},
                    {"coinco"},
                    {"pumanque"},
                    {"placilla"},
                    {"coltauco"},
                    {"palmilla"},
                    {"peumo"},
                    {"estrella"},
                    {"pichilemu"},
                    {"machali"},
                    {"quinta", "tilcoco"},
                    {"doñihue"},
                    {"olivar"},
                    {"graneros"},
                    {"marchihue"},
                    {"peralillo"}
            },
            {
                    {"chanco"},
                    {"licanten"},
                    {"pencahue"},
                    {"constitucion"},
                    {"curico"},
                    {"empedrado"},
                    {"clemente"},
                    {"rafael"},
                    {"retiro"},
                    {"cauquenes"},
                    {"talca"},
                    {"molina"},
                    {"longavi"},
                    {"yerbas", "buenas"},
                    {"javier"},
                    {"linares"},
                    {"teno"},
                    {"hualañe"},
                    {"parral"},
                    {"rio", "claro"},
                    {"pelluhue"},
                    {"romeral"},
                    {"colbun"},
                    {"curepto"},
                    {"pelarco"},
                    {"vichuquen"},
                    {"maule"},
                    {"villa", "alegre"},
                    {"rauco"},
                    {"sagrada", "familia"}
            },
            {
                    {"hualpen"},
                    {"antuco"},
                    {"curanilahue"},
                    {"coronel"},
                    {"chiguayante"},
                    {"lebu"},
                    {"talcahuano"},
                    {"nacimiento"},
                    {"tome"},
                    {"cabrero"},
                    {"pedro", "paz"},
                    {"lota"},
                    {"arauco"},
                    {"concepcion"},
                    {"tucapel"},
                    {"hualqui"},
                    {"cañete"},
                    {"alamos"},
                    {"contulmo"},
                    {"negrete"},
                    {"quilleco"},
                    {"juana"},
                    {"mulchen"},
                    {"barbara"},
                    {"laja"},
                    {"penco"},
                    {"yumbel"},
                    {"alto", "biobio"},
                    {"tirua"},
                    {"angeles"},
                    {"quilaco"},
                    {"florida"},
                    {"rosendo"}
            },
            {
                    {"victoria"},
                    {"melipeuco"},
                    {"ercilla"},
                    {"sauces"},
                    {"teodoro" ,"schmidt"},
                    {"gorbea"},
                    {"pucon"},
                    {"nueva", "imperial"},
                    {"villarrica"},
                    {"lonquimay"},
                    {"lautaro"},
                    {"curarrehue"},
                    {"galvarino"},
                    {"saavedra"},
                    {"lumaco"},
                    {"traiguen"},
                    {"perquenco"},
                    {"cunco"},
                    {"puren"},
                    {"angol"},
                    {"cholchol"},
                    {"temuco"},
                    {"freire"},
                    {"pitrufquen"},
                    {"curacautin"},
                    {"renaico"},
                    {"vilcun"},
                    {"carahue"},
                    {"collipulli"},
                    {"loncoche"},
                    {"tolten"},
                    {"padre", "casas"}
            },
            {
                    {"futaleufu"},
                    {"osorno"},
                    {"rio", "negro"},
                    {"maullin"},
                    {"varas"},
                    {"hualaihue"},
                    {"quemchi"},
                    {"muermos"},
                    {"quinchao"},
                    {"chaiten"},
                    {"montt"},
                    {"palena"},
                    {"curaco", "velez"},
                    {"llanquihue"},
                    {"pablo"},
                    {"queilen"},
                    {"puyehue"},
                    {"quellon"},
                    {"calbuco"},
                    {"juan", "costa"},
                    {"ancud"},
                    {"chonchi"},
                    {"fresia"},
                    {"octay"},
                    {"castro"},
                    {"frutillar"},
                    {"dalcahue"},
                    {"purranque"},
                    {"cochamo"},
                    {"puqueldon"}
            },
            {
                    {"lago", "verde"},
                    {"ohiggins"},
                    {"aysen"},
                    {"chile", "chico"},
                    {"coihaique"},
                    {"cochrane"},
                    {"tortel"},
                    {"cisnes"},
                    {"rio", "ibañez"},
                    {"guaitecas"}
            },
            {
                    {"timaukel"},
                    {"punta", "arenas"},
                    {"primavera"},
                    {"porvenir"},
                    {"cabo", "hornos"},
                    {"rio", "verde"},
                    {"gregorio"},
                    {"natales"},
                    {"antartica"},
                    {"laguna", "blanca"},
                    {"torres", "paine"}
            },
            {
                    {"isla", /*"maipo"*/},
                    {"pedro"},
                    {"peñaflor"},
                    {"Puente Alto"},
                    {"bosque"},
                    {"independencia"},
                    {"santiago", "centro"},
                    {"buin"},
                    {"maria", "pinto"},
                    {"maipu"},
                    {"reina"},
                    {"lampa"},
                    {"renca"},
                    {"pintana"},
                    {"miguel"},
                    {"aguirre", "cerda"},
                    {"padre", "hurtado"},
                    {"pudahuel"},
                    {"ñuñoa"},
                    {"colina"},
                    {"quilicura"},
                    {"conchali"},
                    {"calera","tango"},
                    {"prado"},
                    {"tiltil"},
                    {"macul"},
                    {"bernardo"},
                    {"cerro", "navia"},
                    {"vitacura"},
                    {"alhue"},
                    {"espejo"},
                    {"talagante"},
                    {"ramon"},
                    {"estacion", "central"},
                    {"granja"},
                    {"peñalolen"},
                    {"condes"},
                    {"joaquin"},
                    {"florida"},
                    {"paine"},
                    {"cerrillos"},
                    {"quinta", "normal"},
                    {"jose", /*"maipo"*/},
                    {"melipilla"},
                    {"huechuraba"},
                    {"curacavi"},
                    {"cisterna"},
                    {"recoleta"},
                    {"pirque"},
                    {"monte"},
                    {"providencia"},
                    {"barnechea"}
            },
            {
                    {"lagos"},
                    {"mariquina"},
                    {"lago", "ranco"},
                    {"rio", "bueno"},
                    {"union"},
                    {"futrono"},
                    {"valdivia"},
                    {"mafil"},
                    {"panguipulli"},
                    {"lanco"},
                    {"paillaco"},
                    {"corral"}
            },
            {
                    {"arica"},
                    {"putre"},
                    {"general", "lagos"},
                    {"camarones"}
            },
            {
                    {"coelemu"},
                    {"nicolas"},
                    {"ignacio"},
                    {"treguaco"},
                    {"yungay"},
                    {"ninhue"},
                    {"coihueco"},
                    {"cobquecura"},
                    {/*"chillan",*/ "viejo"},
                    {"carmen"},
                    {"bulnes"},
                    {"pemuco"},
                    {"fabian"},
                    {"pinto"},
                    {"portezuelo"},
                    {"quirihue"},
                    {"ñiquen"},
                    {"chillan"},
                    {"ranquil"},
                    {"quillon"},
                    {"carlos"}
            }

    };

    public static final String[][] COMUNAS = {
            {
                    "Camiña",
                    "Pozo Almonte",
                    "Iquique",
                    "Alto Hospicio",
                    "Colchane",
                    "Huara",
                    "Pica"
            },
            {
                    "María Elena",
                    "Mejillones",
                    "Antofagasta",
                    "Tocopilla",
                    "Ollagüe",
                    "San Pedro de Atacama",
                    "Sierra Gorda",
                    "Calama",
                    "Taltal"
            },
            {
                    "Caldera",
                    "Diego de Almagro",
                    "Huasco",
                    "Copiapó",
                    "Alto del Carmen",
                    "Tierra Amarilla",
                    "Freirina",
                    "Chañaral",
                    "Vallenar"
            },
            {
                    "La Serena",
                    "Andacollo",
                    "Paiguano",
                    "Punitaqui",
                    "Los Vilos",
                    "Salamanca",
                    "Combarbalá",
                    "Ovalle",
                    "Coquimbo",
                    "Monte Patria",
                    "Illapel",
                    "Vicuña",
                    "Río Hurtado",
                    "La Higuera",
                    "Canela"
            },
            {
                    "La Ligua",
                    "Quillota",
                    "Calera",
                    "Villa Alemana",
                    "San Felipe",
                    "Catemu",
                    "Concón",
                    "Santa María",
                    "Rinconada",
                    "Limache",
                    "San Esteban",
                    "Cartagena",
                    "Algarrobo",
                    "Viña del Mar",
                    "El Quisco",
                    "Papudo",
                    "Nogales",
                    "Petorca",
                    "Hijuelas",
                    "Putaendo",
                    "El Tabo",
                    "Calle Larga",
                    "Casablanca",
                    "Los Andes",
                    "Quintero",
                    "La Cruz",
                    "Llaillay",
                    "San Antonio",
                    "Quilpué",
                    "Cabildo",
                    "Santo Domingo",
                    "Isla de Pascua",
                    "Valparaíso",
                    "Panquehue",
                    "Puchuncaví",
                    "Zapallar",
                    "Juan Fernández",
                    "Olmué"
            },
            {
                    "San Fernando",
                    "Pichidegua",
                    "Codegua",
                    "Navidad",
                    "Las Cabras",
                    "Rancagua",
                    "San Vicente",
                    "Litueche",
                    "Chépica",
                    "Malloa",
                    "Lolol",
                    "Rengo",
                    "Chimbarongo",
                    "Requínoa",
                    "Santa Cruz",
                    "Nancagua",
                    "Mostazal",
                    "Paredones",
                    "Coinco",
                    "Pumanque",
                    "Placilla",
                    "Coltauco",
                    "Palmilla",
                    "Peumo",
                    "La Estrella",
                    "Pichilemu",
                    "Machalí",
                    "Quinta de Tilcoco",
                    "Doñihue",
                    "Olivar",
                    "Graneros",
                    "Marchihue",
                    "Peralillo"
            },
            {
                    "Chanco",
                    "Licantén",
                    "Pencahue",
                    "Constitución",
                    "Curicó",
                    "Empedrado",
                    "San Clemente",
                    "San Rafael",
                    "Retiro",
                    "Cauquenes",
                    "Talca",
                    "Molina",
                    "Longaví",
                    "Yerbas Buenas",
                    "San Javier",
                    "Linares",
                    "Teno",
                    "Hualañé",
                    "Parral",
                    "Río Claro",
                    "Pelluhue",
                    "Romeral",
                    "Colbún",
                    "Curepto",
                    "Pelarco",
                    "Vichuquén",
                    "Maule",
                    "Villa Alegre",
                    "Rauco",
                    "Sagrada Familia"
            },
            {
                    "Hualpén",
                    "Antuco",
                    "Curanilahue",
                    "Coronel",
                    "Chiguayante",
                    "Lebu",
                    "Talcahuano",
                    "Nacimiento",
                    "Tomé",
                    "Cabrero",
                    "San Pedro de la Paz",
                    "Lota",
                    "Arauco",
                    "Concepción",
                    "Tucapel",
                    "Hualqui",
                    "Cañete",
                    "Los Álamos",
                    "Contulmo",
                    "Negrete",
                    "Quilleco",
                    "Santa Juana",
                    "Mulchén",
                    "Santa Bárbara",
                    "Laja",
                    "Penco",
                    "Yumbel",
                    "Alto Biobío",
                    "Tirúa",
                    "Los Ángeles",
                    "Quilaco",
                    "Florida",
                    "San Rosendo"
            },
            {
                    "Victoria",
                    "Melipeuco",
                    "Ercilla",
                    "Los Sauces",
                    "Teodoro Schmidt",
                    "Gorbea",
                    "Pucón",
                    "Nueva Imperial",
                    "Villarrica",
                    "Lonquimay",
                    "Lautaro",
                    "Curarrehue",
                    "Galvarino",
                    "Saavedra",
                    "Lumaco",
                    "Traiguén",
                    "Perquenco",
                    "Cunco",
                    "Purén",
                    "Angol",
                    "Cholchol",
                    "Temuco",
                    "Freire",
                    "Pitrufquén",
                    "Curacautín",
                    "Renaico",
                    "Vilcún",
                    "Carahue",
                    "Collipulli",
                    "Loncoche",
                    "Toltén",
                    "Padre las Casas"
            },
            {
                    "Futaleufú",
                    "Osorno",
                    "Río Negro",
                    "Maullín",
                    "Puerto Varas",
                    "Hualaihué",
                    "Quemchi",
                    "Los Muermos",
                    "Quinchao",
                    "Chaitén",
                    "Puerto Montt",
                    "Palena",
                    "Curaco de Vélez",
                    "Llanquihue",
                    "San Pablo",
                    "Queilén",
                    "Puyehue",
                    "Quellón",
                    "Calbuco",
                    "San Juan de la Costa",
                    "Ancud",
                    "Chonchi",
                    "Fresia",
                    "Puerto Octay",
                    "Castro",
                    "Frutillar",
                    "Dalcahue",
                    "Purranque",
                    "Cochamó",
                    "Puqueldón"
            },
            {
                    "Lago Verde",
                    "O’Higgins",
                    "Aysén",
                    "Chile Chico",
                    "Coihaique",
                    "Cochrane",
                    "Tortel",
                    "Cisnes",
                    "Río Ibáñez",
                    "Guaitecas"
            },
            {
                    "Timaukel",
                    "Punta Arenas",
                    "Primavera",
                    "Porvenir",
                    "Cabo de Hornos",
                    "Río Verde",
                    "San Gregorio",
                    "Natales",
                    "Antártica",
                    "Laguna Blanca",
                    "Torres del Paine"
            },
            {
                    "Isla de Maipo",
                    "San Pedro",
                    "Peñaflor",
                    "Puente Alto",
                    "El Bosque",
                    "Independencia",
                    "Santiago Centro",
                    "Buin",
                    "María Pinto",
                    "Maipú",
                    "La Reina",
                    "Lampa",
                    "Renca",
                    "La Pintana",
                    "San Miguel",
                    "Pedro Aguirre Cerda",
                    "Padre Hurtado",
                    "Pudahuel",
                    "Ñuñoa",
                    "Colina",
                    "Quilicura",
                    "Conchalí",
                    "Calera de Tango",
                    "Lo Prado",
                    "Tiltil",
                    "Macul",
                    "San Bernardo",
                    "Cerro Navia",
                    "Vitacura",
                    "Alhué",
                    "Lo Espejo",
                    "Talagante",
                    "San Ramón",
                    "Estación Central",
                    "La Granja",
                    "Peñalolén",
                    "Las Condes",
                    "San Joaquín",
                    "La Florida",
                    "Paine",
                    "Cerrillos",
                    "Quinta Normal",
                    "San José de Maipo",
                    "Melipilla",
                    "Huechuraba",
                    "Curacaví",
                    "La Cisterna",
                    "Recoleta",
                    "Pirque",
                    "El Monte",
                    "Providencia",
                    "Lo Barnechea"
            },
            {
                    "Los Lagos",
                    "Mariquina",
                    "Lago Ranco",
                    "Río Bueno",
                    "La Unión",
                    "Futrono",
                    "Valdivia",
                    "Máfil",
                    "Panguipulli",
                    "Lanco",
                    "Paillaco",
                    "Corral"
            },
            {
                    "Arica",
                    "Putre",
                    "General Lagos",
                    "Camarones"
            },
            {
                    "Coelemu",
                    "San Nicolás",
                    "San Ignacio",
                    "Treguaco",
                    "Yungay",
                    "Ninhue",
                    "Coihueco",
                    "Cobquecura",
                    "Chillán Viejo",
                    "El Carmen",
                    "Bulnes",
                    "Pemuco",
                    "San Fabián",
                    "Pinto",
                    "Portezuelo",
                    "Quirihue",
                    "Ñiquén",
                    "Chillán",
                    "Ránquil",
                    "Quillón",
                    "San Carlos"
            }

    };



    public static boolean validatePattern(String emailStr, Pattern pattern) {
        Matcher matcher = pattern.matcher(emailStr);
        return matcher.find();
    }

    public static boolean validate(String value, String type, String region) {
        boolean valid = true;
        switch (type) {
            case "mail":
                valid = validatePattern(value, VALID_EMAIL_ADDRESS_REGEX);
                break;
            case "rut":
                valid = validatePattern(value, VALID_RUT_REGEX);
                break;
            case "code":
                valid = validatePattern(value, VALID_CODE);
                break;
            case "age":
                valid = value.length() > 0 && value.length() < 3;
                break;
            case "region":
                valid = Arrays.asList(REGIONS).contains(value);
                break;
            case "comuna":
                Log.d("Validator", "Region: " + region);
                String[] comunaList = COMUNAS[Arrays.asList(REGIONS).indexOf(region)];
                valid = Arrays.asList(comunaList).contains(value);
                break;
            default:
                valid = value.length() > 0;
                break;
        }
        return valid;
    }
    public static boolean validate(String value, String type){
        return validate(value, type,"");
    }

    public static String obtainStringFromKeywords(String value, SmartCompareMode mode, String regionSelected){
        String returnValue = "NONE";
        Locale SPANISH = Locale.forLanguageTag("es");
        value = value.toLowerCase(SPANISH);
        value = value.replace('á', 'a').replace('é', 'e').replace('í', 'i')
                .replace('ó', 'o').replace('ú', 'u').replace('ü', 'u');
        value = value.replaceAll("[^a-zñ ]", "");
        List<String> keywords = Arrays.asList(value.split(" "));
        boolean endEarly = false;
        switch (mode){
        //Region
            case COMPARE_REGION:
                //for each region, search coincidences
                for (int i = 0; i < REGIONS_KEYWORDS.length; i++) {
                    //for each region_keyword, check if it's the same
                    endEarly = false;
                    for (int j = 0; j < REGIONS_KEYWORDS[i].length; j++) {
                        //compare with all potential keywords
                        for (String kw: keywords) {
                            if(kw.equals(REGIONS_KEYWORDS[i][j])){
                                endEarly = true;
                                returnValue = REGIONS[i];
                                break;
                            }
                        }
                    }
                    if(endEarly) {
                        break;
                    }
                }
                break;
            case COMPARE_COMUNA:
                //find valid comunas
                String[] comunaRealNames = COMUNAS[Arrays.asList(REGIONS).indexOf(regionSelected)];
                String[][] comunaSelected = COMUNAS_KEYWORDS[Arrays.asList(REGIONS).indexOf(regionSelected)];

                //for each comuna, search coincidences
                for (int i = 0; i < comunaSelected.length; i++) {
                    //for each region_keyword, check if it's the same
                    endEarly = false;
                    for (int j = 0; j < comunaSelected[i].length; j++) {
                        //compare with all potential keywords
                        for (String kw: keywords) {
                            if(kw.equals(comunaSelected[i][j])){
                                endEarly = true;
                                returnValue = comunaRealNames[i];
                                break;
                            }
                        }
                    }
                    if(endEarly) {
                        break;
                    }
                }
                break;
            default:
                break;
        }
        return returnValue;
    }
}
