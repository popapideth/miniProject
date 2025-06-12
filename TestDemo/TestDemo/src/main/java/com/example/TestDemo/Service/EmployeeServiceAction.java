package com.example.TestDemo.Service;

import com.example.TestDemo.Entity.Employee;
import com.example.TestDemo.Repositry.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceAction implements EmployeeService{

    private EmployeeRepository employeeRepository;
    @Autowired
    public EmployeeServiceAction(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    //บันทึกข้อมูล มี2แบบเลย (Create & Update)
    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
    //ดึงข้อมูลทั้งหมด (Read)
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    //ดึงข้อมูลตามที่เราสนใจ ใช้empId (Read)
    @Override
    public Employee findEmployeeById(Long empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("ไม่พบข้อมูลพนักงาน " + empId));
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้empName (Read)
    @Override
    public List<Employee> findEmployeeByEmpName(String empName) {
        List<Employee> check = employeeRepository.findEmployeeByEmpName(empName);
        if(check.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลพนักงานชื่อ" + empName);
        }
        return check;
    }
    //ดึงข้อมูลตามที่เราสนใจ ใช้empAge (Read)
    @Override
    public List<Employee> findEmployeeByEmpAge(Integer empAge) {
        List<Employee> check = employeeRepository.findEmployeeByEmpAge(empAge);
        if(check.isEmpty()){
            throw new RuntimeException("ไม่พบข้อมูลพนักงานอายุ" + empAge);
        }
        return check;
    }

    //ลบ ใช้empId(Delete)
    @Override
    public void deleteEmployeeById(Long empId) {
        employeeRepository.deleteById(empId);
    }
}
