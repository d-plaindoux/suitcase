package org.smallibs.suitcase.cases.lang;

import org.smallibs.suitcase.cases.Case;
import org.smallibs.suitcase.cases.Case.WithoutCapture;
import org.smallibs.suitcase.cases.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Strings {

    static WithoutCapture<String, List<String>> Regex(String expression) {
        return WithoutCapture.adapt(new RegularExpression(Pattern.compile(expression)));
    }

    class RegularExpression implements Case<String, Result.WithoutCapture<List<String>>> {
        private final Pattern expression;

        public RegularExpression(Pattern expression) {
            this.expression = expression;
        }

        @Override
        public Optional<Result.WithoutCapture<List<String>>> unapply(String s) {
            final Matcher matcher = expression.matcher(s);
            if (matcher.matches()) {
                final List<String> strings = new ArrayList<>();
                for (int i = 0; i <= matcher.groupCount(); i += 1) {
                    strings.add(s.substring(matcher.start(i), matcher.end(i)));
                }
                return Optional.of(Result.success(strings));
            } else {
                return Optional.empty();
            }
        }
    }
}
