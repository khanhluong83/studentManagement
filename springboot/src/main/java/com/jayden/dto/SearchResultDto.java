package com.jayden.dto;

import java.util.List;

public record SearchResultDto<T>(long count, List<T> items) {

}
