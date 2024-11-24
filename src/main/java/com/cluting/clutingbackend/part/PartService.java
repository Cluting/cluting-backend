package com.cluting.clutingbackend.part;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartService {
    private final PartRepository partRepository;
}
