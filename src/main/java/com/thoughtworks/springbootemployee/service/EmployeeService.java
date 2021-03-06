package com.thoughtworks.springbootemployee.service;

import com.thoughtworks.springbootemployee.dto.EmployeeRequestDto;
import com.thoughtworks.springbootemployee.dto.EmployeeResponseDto;
import com.thoughtworks.springbootemployee.entity.Company;
import com.thoughtworks.springbootemployee.entity.Employee;
import com.thoughtworks.springbootemployee.exception.CompanyNotFoundException;
import com.thoughtworks.springbootemployee.exception.EmployeeNotFoundException;
import com.thoughtworks.springbootemployee.repository.CompanyRepository;
import com.thoughtworks.springbootemployee.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.thoughtworks.springbootemployee.dto.EmployeeResponseDto.from;

@Service
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final CompanyRepository companyRepository;

  public EmployeeService(
      EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
    this.employeeRepository = employeeRepository;
    this.companyRepository = companyRepository;
  }

  public EmployeeResponseDto getEmployee(int id) {
    return from(employeeRepository.findById(id).orElseThrow(EmployeeNotFoundException::new));
  }

  public List<EmployeeResponseDto> getEmployeeByGender(String gender) {
    return employeeRepository.findByGender(gender).stream()
        .map(EmployeeResponseDto::from)
        .collect(Collectors.toList());
  }

  public Page<Employee> getEmployeeByPage(Pageable pageable) {
    return employeeRepository.findAll(pageable);
  }

  public Employee addEmployee(EmployeeRequestDto employeeRequestDto) {
    Company company =
        companyRepository
            .findById(employeeRequestDto.getCompanyId())
            .orElseThrow(CompanyNotFoundException::new);
    Employee employee = new Employee();
    employee.setGender(employeeRequestDto.getGender());
    employee.setAge(employeeRequestDto.getAge());
    employee.setName(employeeRequestDto.getName());
    employee.setCompany(company);
    return employeeRepository.save(employee);
  }

  public Employee updateEmployee(int id, EmployeeRequestDto employeeRequestDto) {
    Company company =
        companyRepository
            .findById(employeeRequestDto.getCompanyId())
            .orElseThrow(CompanyNotFoundException::new);
    Employee employee = new Employee();
    employee.setId(id);
    employee.setCompany(company);
    employee.setGender(employeeRequestDto.getGender());
    employee.setAge(employeeRequestDto.getAge());
    employee.setName(employeeRequestDto.getName());
    return employeeRepository.save(employee);
  }

  public void deleteEmployee(int id) {
    employeeRepository.deleteById(id);
  }
}
