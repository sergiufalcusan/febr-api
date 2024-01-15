package io.febr.api.controller.exception;

import java.util.List;

public record ApiException(List<String> errors) {
}
