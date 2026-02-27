@"
package com.neera;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;

@SpringBootApplication
@RestController
@RequestMapping("/api/estimate")
public class Application {
    @Autowired
    private JdbcTemplate db;
    public static void main(String[] args) { SpringApplication.run(Application.class, args); }
    @PostMapping
    public Map<String, Object> calculate(@RequestBody Map<String, String> req) {
        Double hm = db.queryForObject("SELECT multiplier_value FROM neera_multipliers WHERE type_key = ?", Double.class, req.get("homeType"));
        Double pkg = db.queryForObject("SELECT multiplier_value FROM neera_multipliers WHERE type_key = ?", Double.class, req.get("packageType"));
        Double base = db.queryForObject("SELECT base_price FROM neera_base_interior_lookup WHERE home_type = ? AND package_type = ?", Double.class, req.get("homeType"), req.get("packageType"));
        Map<String, Object> res = new HashMap<>();
        res.put("total", Math.round(base * hm * pkg));
        return res;
    }
}
"@ | Out-File -FilePath src/main/java/com/neera/Application.java -Encoding utf8
