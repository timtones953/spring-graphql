package com.example.graphql.type;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.FloatValue;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@DgsScalar(name="BigDecimal")
public class BigDecimalScalar implements Coercing<BigDecimal, String> {
    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDate) {
            return ((LocalDate) dataFetcherResult).format(DateTimeFormatter.ISO_DATE_TIME);
        } else {
            throw new CoercingSerializeException("Value is not a valid BigDecimal");
        }
    }

    @Override
    public BigDecimal parseValue(Object input) throws CoercingParseValueException {
        return  (BigDecimal) input;
    }

    @Override
    public BigDecimal parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof FloatValue value) {
            return value.getValue();
        }

        throw new CoercingParseLiteralException("Value is not a valid BigDecimal");
    }

    @NotNull
    @Override
    public Value valueToLiteral(@NotNull Object input) {
        return new StringValue(this.serialize(input));
    }
}