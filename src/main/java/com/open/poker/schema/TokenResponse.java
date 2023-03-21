package com.open.poker.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    @NonNull @With private String token;
    @NonNull private final String tokenType = "Bearer";
}
