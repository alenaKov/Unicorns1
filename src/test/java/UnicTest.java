import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class UnicTest {
    @Test
    public void UserCouldCreateUnicorn(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Ernesta\", \"color\": \"Purple\"}")
                .when()
                .post("https://crudcrud.com/api/64aa5bbd3266472d9ca1ae387ab2b933/unicorn")
                .then()
                .assertThat()
                .statusCode(201)
                .body("$", hasKey("_id"));

    }

    @Test
    public void UserCouldDeleteUnicorn(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        String id =given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Ernesta\", \"color\": \"Purple\"}")
                .when()
                .post("https://crudcrud.com/api/64aa5bbd3266472d9ca1ae387ab2b933/unicorn")
                .then()
                .assertThat()
                .statusCode(201)
                .body("$", hasKey("_id"))
                .extract()
                        .path("_id");

        //delete
        given()
                .delete("https://crudcrud.com/api/64aa5bbd3266472d9ca1ae387ab2b933/unicorn/" + id)
                .then()
                .assertThat()
                .statusCode(200);
        //verify not exists

        given()
                .get("https://crudcrud.com/api/64aa5bbd3266472d9ca1ae387ab2b933/unicorn/" + id)
                .then()
                .assertThat()
                .statusCode(404);


    }

    @Test
    public void UserCouldUpdateUnicornColor() {
        // Логирование запросов и ответов
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        // 1. Создаем нового единорога
        String id = given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"Polly\", \"color\": \"Blue\"}")
                .when()
                .post("https://crudcrud.com/api/64aa5bbd3266472d9ca1ae387ab2b933/unicorn")
                .then()
                .assertThat()
                .statusCode(201)
                .body("$", hasKey("_id"))
                .extract()
                .path("_id");

        // 2. Обновляем цвет единорога (PUT/PATCH запрос)
        given()
                .contentType(ContentType.JSON)
                .body("{\"color\": \"Pink\"}")  // Только изменяемые поля
                .when()
                .put("https://crudcrud.com/api/64aa5bbd3266472d9ca1ae387ab2b933/unicorn/" + id)
                .then()
                .assertThat()
                .statusCode(200);

        // 3. Проверяем, что цвет действительно изменился
        given()
                .get("https://crudcrud.com/api/64aa5bbd3266472d9ca1ae387ab2b933/unicorn/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("color", equalTo("Pink"));  // Проверка нового цвета
    }
}


