package data;

import java.util.LinkedHashMap;
import java.util.Map;

public class CoffeeConstants {


    // Назви для UI (ключ → українська)

    public static final Map<String, String> BREW_UA = new LinkedHashMap<>();
    public static final Map<String, String> TEMP_UA = new LinkedHashMap<>();
    public static final Map<String, String> ORIGIN_UA = new LinkedHashMap<>();


    // Для конструктора рецепту (українська → ключ)

    public static final Map<String, String> BREW_MAP = new LinkedHashMap<>();
    public static final Map<String, String> TEMP_MAP = new LinkedHashMap<>();
    public static final Map<String, String> ORIGIN_MAP = new LinkedHashMap<>();

    // Параметри кави

    public static final Map<String, Double> BREW_VOLUME     = new LinkedHashMap<>();
    public static final Map<String, String> BREW_INGREDIENT = new LinkedHashMap<>();

    static {
        // brew — українська назва
        BREW_UA.put("espresso",        "Еспресо");
        BREW_UA.put("filter",          "Фільтр");
        BREW_UA.put("cold_brew",       "Колд брю");
        BREW_UA.put("aeropress",       "Аеропрес");
        BREW_UA.put("moka_pot",        "Мока");
        BREW_UA.put("chemex",          "Кемекс");
        BREW_UA.put("v60",             "V60");
        BREW_UA.put("french_press",    "Французький прес");
        BREW_UA.put("turkish_coffee",  "Турецька кава");
        BREW_UA.put("siphon",          "Сифон");
        BREW_UA.put("nitro_cold_brew", "Нітро колд брю");

        // brew — зворотній (для ComboBox/RadioButton)
        BREW_UA.forEach((k, v) -> BREW_MAP.put(v, k));

        // температура
        TEMP_UA.put("hot",  "Гаряча");
        TEMP_UA.put("cold", "Холодна");
        TEMP_UA.forEach((k, v) -> TEMP_MAP.put(v, k));

        // origin
        ORIGIN_UA.put("ethiopia",  "Ефіопія");
        ORIGIN_UA.put("colombia",  "Колумбія");
        ORIGIN_UA.put("brazil",    "Бразилія");
        ORIGIN_UA.put("guatemala", "Гватемала");
        ORIGIN_UA.put("kenya",     "Кенія");
        ORIGIN_UA.put("indonesia", "Індонезія");
        ORIGIN_UA.forEach((k, v) -> ORIGIN_MAP.put(v, k));

        // обʼєм кави залежно від методу
        BREW_VOLUME.put("espresso",        50.0);
        BREW_VOLUME.put("filter",         250.0);
        BREW_VOLUME.put("cold_brew",      200.0);
        BREW_VOLUME.put("aeropress",      150.0);
        BREW_VOLUME.put("moka_pot",        80.0);
        BREW_VOLUME.put("chemex",         300.0);
        BREW_VOLUME.put("v60",            250.0);
        BREW_VOLUME.put("french_press",   300.0);
        BREW_VOLUME.put("turkish_coffee",  80.0);
        BREW_VOLUME.put("siphon",         200.0);
        BREW_VOLUME.put("nitro_cold_brew",200.0);

        // ключ інгредієнта кави для кожного методу
        BREW_INGREDIENT.put("espresso",        "espresso");
        BREW_INGREDIENT.put("filter",          "filter_coffee");
        BREW_INGREDIENT.put("cold_brew",       "cold_brew");
        BREW_INGREDIENT.put("aeropress",       "aeropress");
        BREW_INGREDIENT.put("moka_pot",        "moka_pot");
        BREW_INGREDIENT.put("chemex",          "chemex");
        BREW_INGREDIENT.put("v60",             "v60");
        BREW_INGREDIENT.put("french_press",    "french_press");
        BREW_INGREDIENT.put("turkish_coffee",  "turkish_coffee");
        BREW_INGREDIENT.put("siphon",          "siphon");
        BREW_INGREDIENT.put("nitro_cold_brew", "nitro_cold_brew");
    }

    public static String brewName(String key) {
        return BREW_UA.getOrDefault(key, key);
    }

    public static String tempName(String key) {
        return TEMP_UA.getOrDefault(key, key);
    }

}