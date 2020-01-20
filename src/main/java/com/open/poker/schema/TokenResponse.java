package com.open.poker.schema;

import lombok.NonNull;
import lombok.Value;
import lombok.With;

@Value
public class TokenResponse {
    @NonNull @With private final String token;
    @NonNull private final String tokenType = "Bearer";
}
