package org.example.movie_rental.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.movie_rental.dto.StaffLoginResponse;
import org.example.movie_rental.entity.Staff;
import org.example.movie_rental.exception.BusinessException;
import org.example.movie_rental.repository.StaffRepository;
import org.example.movie_rental.service.StaffAuthService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffAuthServiceImpl implements StaffAuthService {

    private final StaffRepository staffRepository;

    @Override
    public StaffLoginResponse login(String email, String password) {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!password.equals(staff.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        return new StaffLoginResponse(
                staff.getStaffId(),
                staff.getFirstName(),
                staff.getLastName(),
                staff.getEmail(),
                staff.getActive()
        );
    }
}
