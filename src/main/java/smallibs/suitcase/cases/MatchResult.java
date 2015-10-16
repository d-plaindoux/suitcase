/*
 * Copyright (C)2015 D. Plaindoux.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; see the file COPYING.  If not, write to
 * the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package smallibs.suitcase.cases;

import java.util.ArrayList;
import java.util.List;

public class MatchResult {

    private final List<Object> bindings;
    private final Object returnedObject;

    public MatchResult(Object returnedObject) {
        this.bindings = new ArrayList<>();
        this.returnedObject = returnedObject;
    }

    public MatchResult(Object binding, Object returnedObject) {
        this.bindings = new ArrayList<>();
        this.bindings.add(binding);
        this.returnedObject = returnedObject;
    }

    public MatchResult with(MatchResult result) {
        this.bindings.addAll(result.bindings);
        return this;
    }

    public List<Object> bindings() {
        return bindings;
    }

    public Object matchedObject() {
        return returnedObject;
    }
}