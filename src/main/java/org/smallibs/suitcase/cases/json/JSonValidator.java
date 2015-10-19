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

package org.smallibs.suitcase.cases.json;

import java.util.Optional;

public class JSonValidator implements JSonHandler<Boolean, Boolean, Boolean, Boolean, Boolean> {
    @Override
    public Boolean anObject(Optional<Boolean> members) {
        return !members.isPresent() || members.get();
    }

    @Override
    public Boolean anArray(Optional<Boolean> values) {
        return !values.isPresent() || values.get();
    }

    @Override
    public Boolean someMembers(Boolean o1, Optional<Boolean> o2) {
        return o1 && (!o2.isPresent() || o2.get());
    }

    @Override
    public Boolean aMember(String o1, Boolean o2) {
        return o2;
    }

    @Override
    public Boolean someValues(Boolean o1, Optional<Boolean> o2) {
        return o1 && (!o2.isPresent() || o2.get());
    }

    @Override
    public Boolean anInteger(int i) {
        return true;
    }

    @Override
    public Boolean aString(String s) {
        return true;
    }

    @Override
    public Boolean aFloat(float f) {
        return true;
    }

    @Override
    public Boolean aNull() {
        return true;
    }

    @Override
    public Boolean aBoolean(boolean b) {
        return true;
    }

    @Override
    public Boolean aValue(Boolean o) {
        return o;
    }
}
