package smallibs.suitcase.annotations;

import smallibs.suitcase.pattern.utils.Lists;

public @interface List {
    final Class model = Lists.Empty.getClass();
}
