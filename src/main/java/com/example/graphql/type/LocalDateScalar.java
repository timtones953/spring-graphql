package com.example.graphql.type;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@DgsScalar(name="LocalDate")
public class LocalDateScalar implements Coercing<LocalDate, String> {
    @Override
    public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof LocalDate) {
            return ((LocalDate) dataFetcherResult).format(DateTimeFormatter.ISO_DATE_TIME);
        } else {
            throw new CoercingSerializeException("Not a valid DateTime");
        }
    }

    @Override
    public LocalDate parseValue(Object input) throws CoercingParseValueException {
        return LocalDate.parse(input.toString(), DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
            return LocalDate.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_DATE_TIME);
        }

        throw new CoercingParseLiteralException("Value is not a valid ISO date time");
    }

    @NotNull
    @Override
    public Value valueToLiteral(@NotNull Object input) {
        return new StringValue(this.serialize(input));
    }
}