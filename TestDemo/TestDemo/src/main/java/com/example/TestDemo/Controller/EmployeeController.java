package com.example.TestDemo.Controller;

import com.example.TestDemo.Entity.Employee;
import com.example.TestDemo.Service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    //บันทึกข้อมูล จะไม่มีidส่งมาด้วย (Create)
    @PostMapping("/addEmployee")
    public Employee addEmployee(@RequestBody Employee employee){
      employee.setEmpId(null);
      return employeeService.save(employee);
    }

    //ดึงข้อมูลทั้งหมด (Read)
    @GetMapping
    public List<Employee> getAllEmployee(){
        return employeeService.findAll();
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้empId (Read)
    @GetMapping("/id/{empId}")
    public Employee getEmplyeeById(@PathVariable Long empId){
        Employee myEmplyee = employeeService.findEmployeeById(empId);
        if(myEmplyee == null){
            throw new RuntimeException("ไม่พบข้อมูลพนักงาน " + empId);
        }
        return myEmplyee;
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้empName (Read)
    @GetMapping("/name/{empName}")
    public List<Employee> getEmplyeeByName(@PathVariable String empName){
        List<Employee> myEmployee = employeeService.findEmployeeByEmpName(empName);
        if(myEmployee == null || myEmployee.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลพนักงานชื่อ" + empName);
        }
        return myEmployee;
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้empAge (Read)
    @GetMapping("/age/{empAge}")
    public List<Employee> getEmplyeeByAge(@PathVariable Integer empAge){
        List<Employee> myEmployee = employeeService.findEmployeeByEmpAge(empAge);
        if(myEmployee == null || myEmployee.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลพนักงานอายุ" + empAge);
        }
        return myEmployee;
    }
    //ลบ ใช้empId(Delete)
    @DeleteMapping("/{empId}")
    public String deleteEmployee(@PathVariable Long empId){
        Employee myEmployee = employeeService.findEmployeeById(empId);
        if (myEmployee == null){
            throw new RuntimeException("ไม่พบข้อมูลพนักงาน " + empId);
        }
        employeeService.deleteEmployeeById(empId);
        return "ลบข้อมูลพนักงานที่" + empId +"เรียบร้อยแล้ว";
    }

    //อัพเดทข้อมูล จะมีidส่งมาด้วย(update)
    @PutMapping("/update")
    public Employee updateEmployee(@RequestBody Employee employee){
        return employeeService.save(employee);
    }
}