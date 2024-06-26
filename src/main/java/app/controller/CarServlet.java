package app.controller;

import app.domain.Car;
import app.repository.CarRepository;
import app.repository.CarRepositoryMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarServlet extends HttpServlet {

    private CarRepository repository = new CarRepositoryMap();

    // GET http://10.2.3.4:8080/cars
    // GET http://10.2.3.4:8080/cars?id=5

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Для получения из БД всех или одного автомобиля по id

        // req - объект запроса, из него мы можем извлечь всё, что прислал клиент
        // resp - объект ответа, который будет отправлен клиенту после того,
        //        как отработает наш метод. И мы можем в этот объект поместить всю
        //        информацию, которую мы хотим отправить клиенту в ответ на его запрос.
        String idParam = req.getParameter("id");
        if (idParam != null) {
            resp.getWriter().write("Hello Car " + idParam + "!");
            try{
                long id=Long.parseLong(idParam);
                Car car = repository.getCarById(id);
                if(car != null) {
                    //resp.setContentType("application/json");
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(car);
                    //resp.getWriter().write(json);
                    resp.getWriter().write("Car is found");

                }
                else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("Car not found");
                }
            }
             catch (Exception e)
             {
                 //TODO handle exception
             }

        }
        else {
            List<Car> cars = repository.getAllCars();
            cars.forEach(x -> {
                try {
                    resp.getWriter().write(x.toString() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // TODO Домашнее задание:
        // Реализовать получение одного автомобиля по id
//         Map<String, String[]> parameterMap = req.getParameterMap();
//         parameterMap.forEach((k, v) -> {
//             System.out.println("key: " + k );
//             for (int i = 0; i < v.length; i++) {
//                 System.out.println("value: " + v[i]);
//
//             }
//         });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Для сохранения нового автомобиля в БД

        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(req.getReader(), Car.class);
        repository.saveCar(car);
        resp.getWriter().write(car.toString());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Для изменения существующего автомобиля в БД

        // TODO Домашнее задание:
        // Реализовать изменение объекта автомобиля в БД
        // (при этом меняться должна только цена)
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Для удаления автомобиля из БД

        // TODO Домашнее задание:
        // Реализовать удаление автомобиля из БД по id
    }
}