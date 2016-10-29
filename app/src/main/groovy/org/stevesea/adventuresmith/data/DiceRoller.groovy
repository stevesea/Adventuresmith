/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of Adventuresmith.
 *
 * Adventuresmith is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Adventuresmith is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adventuresmith.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.data

class DiceRoller {
    public static Map<String, AbstractGenerator> generators = [
            '1d6':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 6, getShuffler().random)
                    return "${ss(dice.toString())} : ${dice.roll()}"
                }
            },
            '1d8':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 8, getShuffler().random)
                    return "${ss(dice.toString())} : ${dice.roll()}"
                }
            },
            '1d10':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 10, getShuffler().random)
                    return "${ss(dice.toString())} : ${dice.roll()}"
                }
            },
            '1d12':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 12, getShuffler().random)
                    return "${ss(dice.toString())} : ${dice.roll()}"
                }
            },
            '1d20':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 20, getShuffler().random)
                    return "${ss(dice.toString())} : ${dice.roll()}"
                }
            },
            '1d30':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 30, getShuffler().random)
                    return "${ss(dice.toString())} : ${dice.roll()}"
                }
            },
            '1d100':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 100, getShuffler().random)
                    return "${ss(dice.toString())} : ${dice.roll()}"
                }
            },
            '2d6':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 6, getShuffler().random)
                    def rolls = [dice.roll(), dice.roll()]
                    return "${ss("2d6")} : ${rolls.sum()} ${small(rolls.toString())}"
                }
            },
            '3d6':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 6, getShuffler().random)
                    def rolls = [dice.roll(), dice.roll(), dice.roll()]
                    return "${ss("3d6")} : ${rolls.sum()} ${small(rolls.toString())}"
                }
            },
            '4d4':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 4, getShuffler().random)
                    def rolls = [dice.roll(), dice.roll(), dice.roll(), dice.roll()]
                    return "${ss("4d4")} : ${rolls.sum()} ${small(rolls.toString())}"
                }
            },
            '1d20adv':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 20, getShuffler().random)
                    def rolls = [dice.roll(), dice.roll()]

                    return "${ss(dice.toString() + " adv")} : ${Collections.max(rolls)} ${small(rolls.toString())}"
                }
            },
            '1d20disadv':  new AbstractGenerator() {
                @Override
                String generate() {
                    def dice = Dice.dice(1, 20, getShuffler().random)
                    def rolls = [dice.roll(), dice.roll()]

                    return "${ss(dice.toString() + " disadv")} : ${Collections.min(rolls)} ${small(rolls.toString())}"
                }
            },
        ]
}
