/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.rpgpad.data.stars_without_number

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator
import org.stevesea.rpgpad.data.RangeMap
import org.stevesea.rpgpad.data.Shuffler

@CompileStatic
class SWNWorld extends AbstractGenerator {

    SWNnames names = new SWNnames()
    SWNpolitical_party political = new SWNpolitical_party()

    @Override
    AbstractGenerator withShuffler(Shuffler shuff) {
        super.withShuffler(shuff)
        names.withShuffler(shuff)
        political.withShuffler(shuff)
        this
    }

    RangeMap atmospheres = new RangeMap()
            .with(2, 'Corrosive')
            .with(3, 'Inert gas')
            .with(4, 'Airless or thin atmosphere')
            .with(5..9, 'Breatheable mix')
            .with(10, 'Thick atmosphere, breathable with a pressure mask')
            .with(11, 'Invasive, toxic atmosphere')
            .with(12, 'Corrosive and invasive atmosphere')
    RangeMap temperatures = new RangeMap()
            .with(2, 'Frozen')
            .with(3, 'Variable cold-to-temperate')
            .with(4..5, 'Cold')
            .with(6..8, 'Temperate')
            .with(9..10, 'Warm')
            .with(11, 'Variable temperate-to-warm')
            .with(12, 'Burning')
    RangeMap biospheres = new RangeMap()
            .with(2, 'Biosphere remnants')
            .with(3, 'Microbial life')
            .with(4..5, 'No native biosphere')
            .with(6..8, 'Human-miscible biosphere')
            .with(9..10, 'Immiscible biosphere')
            .with(11, 'Hybrid biosphere')
            .with(12, 'Engineered biosphere')

    RangeMap populations = new RangeMap()
            .with(2, 'Failed colony')
            .with(3, 'Outpost')
            .with(4..5, 'Tens of thousands of inhabitants')
            .with(6..8, 'Hundreds of thousands of inhabitants')
            .with(9..10, 'Millions of inhabitants')
            .with(11, 'Billions of inhabitants')
            .with(12, 'Alien civilization')
    RangeMap techlevels = new RangeMap()
            .with(2, 'Tech Level 0. Stone-age technology.')
            .with(3, 'Tech level 1. Medieval technology.')
            .with(4, 'Tech level 2. Nineteenth-century technology.')
            .with(5..6, 'Tech level 3. Twentieth-century technology.')
            .with(7..10, 'Tech level 4. Baseline postech.')
            .with(11, 'Tech level 4 with specialties or some surviving pretech.')
            .with(12, 'Tech level 5. Pretech, pre-Silence technology.')

    @Canonical
    public static class WorldTag {
        String flavor
        List<String> enemies = new ArrayList<>()
        List<String> friends = new ArrayList<>()
        List<String> complications = new ArrayList<>()
        List<String> things = new ArrayList<>()
        List<String> places = new ArrayList<>()

        WorldTag accumulate(WorldTag other) {
            enemies.addAll(other.enemies)
            friends.addAll(other.friends)
            complications.addAll(other.complications)
            things.addAll(other.things)
            places.addAll(other.places)

            this
        }
    }
    Map<String, WorldTag> world_tags = [
            'Abandoned Colony'    : new WorldTag(
                    flavor: '''The world once hosted a colony, whether human or otherwise, until some crisis or natural disaster drove the inhabitants away or killed them off . The
colony might have been mercantile in nature, an expedition to extract valuable local resources, or it might have been a reclusive cabal of zealots. The
remains of the colony are usually in ruins, and might still be dangerous from the aftermath of whatever destroyed it in the first place.''',
                    enemies: 'Crazed survivors, Ruthless plunderers of the ruins, Automated defense system'.tokenize(',').collect { it.trim() },
                    friends: 'Inquisitive stellar archaeologist, Heir to the colony’s property, Local wanting the place cleaned out'.tokenize(',').collect { it.trim() },
                    complications: 'The local government wants the ruins to remain a secret, The locals claim ownership of it, The colony is crumbling and dangerous to navigate'.tokenize(',').collect { it.trim() },
                    things: 'Long-lost property deeds, Relic stolen by the colonists when they left, Historical record of the colonization attempt'.tokenize(',').collect { it.trim() },
                    places: 'Decaying habitation block, Vine-covered town square, Structure buried by an ancient landslide'.tokenize(',').collect { it.trim() },
            ),
            'Alien Ruins'         : new WorldTag(
                    flavor: '''The world has significant alien ruins present. The locals may or may not permit others to investigate the ruins, and may make it difficult to remove
any objects of value without substantial payment.''',
                    enemies: 'Customs inspector, Worshipper of the ruins, Hidden alien survivor'.tokenize(',').collect { it.trim() },
                    friends: 'Curious scholar, Avaricious local resident, Interstellar smuggler'.tokenize(',').collect { it.trim() },
                    complications: 'Traps in the ruins, Remote location, Paranoid customs officials'.tokenize(',').collect { it.trim() },
                    things: 'Precious alien artifacts, Objects left with the remains of a prior unsuccessful expedition, Untranslated alien texts, Untouched hidden ruins'.tokenize(',').collect { it.trim() },
                    places: 'Undersea ruin, Orbital ruin, Perfectly preserved alien building, Alien mausoleum'.tokenize(',').collect { it.trim() }
            ),
            'Altered Humanity'    : new WorldTag(
                    flavor: '''The humans on this world are visibly and drastically different from normal humanity. They may have additional limbs, new sensory organs, or other
significant changes. Were these from ancestral eugenic manipulation, or from environmental toxins?''',
                    enemies: 'Biochauvinist local, Local experimenter, Mentally unstable mutant'.tokenize(',').collect { it.trim() },
                    friends: 'Local seeking a “cure”, Curious xenophiliac, Anthropological researcher'.tokenize(',').collect { it.trim() },
                    complications: 'Alteration is contagious, Alteration is necessary for long-term survival, Locals fear and mistrust non-local humans'.tokenize(',').collect { it.trim() },
                    things: 'Original pretech mutagenic equipment, Valuable biological byproduct from the mutants, “Cure” for the altered genes, Record of the original colonial genotypes'.tokenize(',').collect { it.trim() },
                    places: 'Abandoned eugenics laboratory, An environment requiring the mutation for survival, A sacred site where the first local was transformed'.tokenize(',').collect { it.trim() }
            ),
            'Area 51'             : new WorldTag(
                    flavor: '''The world’s government is fully aware of their local stellar neighbors, but the common populace has no idea about it- and the government means to
keep it that way. Trade with government officials in remote locations is possible, but any attempt to clue the commoners in on the truth will be met
with lethal reprisals.''',
                    enemies: 'Suspicious government minder, Free merchant who likes his local monopoly, Local who wants a specimen for dissection'.tokenize(',').collect { it.trim() },
                    friends: 'Crusading offworld investigator, Conspiracy-theorist local, Idealistic government reformer'.tokenize(',').collect { it.trim() },
                    complications: 'The government has a good reason to keep the truth concealed, The government ruthlessly oppresses the natives, The government is actually composed of offworlders'.tokenize(',').collect { it.trim() },
                    things: 'Elaborate spy devices, Memory erasure tech, Possessions of the last offworlder who decided to spread the truth'.tokenize(',').collect { it.trim() },
                    places: 'Desert airfield, Deep subterranean bunker, Hidden mountain valley'.tokenize(',').collect { it.trim() }
            ),
            'Badlands World'      : new WorldTag(
                    flavor: '''Whatever the ostensible climate and atmosphere type, something horrible happened to this world. Biological, chemical, or nanotechnical weaponry
has reduced it to a wretched hellscape''',
                    enemies: 'Mutated badlands fauna, Desperate local, Badlands raider chief'.tokenize(',').collect { it.trim() },
                    friends: 'Native desperately wishing to escape the world, Scientist researching ecological repair methods, Ruin scavenger'.tokenize(',').collect { it.trim() },
                    complications: 'Radioactivity, Bioweapon traces, Broken terrain, Sudden local plague'.tokenize(',').collect { it.trim() },
                    things: 'Maltech research core, Functional pretech weaponry, An uncontaminated well'.tokenize(',').collect { it.trim() },
                    places: 'Untouched oasis, Ruined city, Salt flat'.tokenize(',').collect { it.trim() }
            ),
            'Bubble Cities'       : new WorldTag(
                    flavor: '''Whether due to a lack of atmosphere or an uninhabitable climate, the world’s cities exist within domes or pressurized buildings. In such sealed
environments, techniques of surveillance and control can grow baroque and extreme.''',
                    enemies: 'Native dreading outsider contamination, Saboteur from another bubble city, Local official hostile to outsider ignorance of laws'.tokenize(',').collect { it.trim() },
                    friends: 'Local rebel against the city officials, Maintenance chief in need of help, Surveyor seeking new building sites'.tokenize(',').collect { it.trim() },
                    complications: 'Bubble rupture, Failing atmosphere reprocessor, Native revolt against officials, All-seeing surveillance cameras'.tokenize(',').collect { it.trim() },
                    things: 'Pretech habitat technology, Valuable industrial products, Master key codes to a city’s security system'.tokenize(',').collect { it.trim() },
                    places: 'City power core, Surface of the bubble, Hydroponics complex, Warren-like hab block'.tokenize(',').collect { it.trim() }
            ),
            'Civil War'           : new WorldTag(
                    flavor: '''The world is currently torn between at least two opposing factions, all of which claim legitimacy. The war may be the result of a successful rebel
uprising against tyranny, or it might just be the result of schemers who plan to be the new masters once the revolution is complete.''',
                    enemies: 'Faction commissar, Angry native, Conspiracy theorist who blames offworlders for the war, Deserter looking out for himself, Guerilla bandits'.tokenize(',').collect { it.trim() },
                    friends: 'Faction loyalist seeking aid, Native caught in the crossfire, Offworlder seeking passage off the planet'.tokenize(',').collect { it.trim() },
                    complications: 'The front rolls over the group, Famine strikes, Bandit infestations'.tokenize(',').collect { it.trim() },
                    things: 'Ammo dump, Military cache, Treasure buried for after the war, Secret war plans'.tokenize(',').collect { it.trim() },
                    places: 'Battle front, Bombed-out town, Rear-area red light zone, Propaganda broadcast tower'.tokenize(',').collect { it.trim() }
            ),
            'Cold War'            : new WorldTag(
                    flavor: '''Two or more great powers control the planet, and they have a hostility to each other that’s just barely less than open warfare. The hostility might be
ideological in nature, or it might revolve around control of some local resource.''',
                    enemies: 'Suspicious chief of intelligence, Native who thinks the outworlders are with the other side, Femme fatale'.tokenize(',').collect { it.trim() },
                    friends: 'Apolitical information broker, Spy for the other side, Unjustly accused innocent, “He’s a bastard, but he’s our bastard” official'.tokenize(',').collect { it.trim() },
                    complications: 'Police sweep, Low-level skirmishing, “Red scare”'.tokenize(',').collect { it.trim() },
                    things: 'List of traitors in government, secret military plans, Huge cache of weapons built up in preparation for war'.tokenize(',').collect { it.trim() },
                    places: 'Seedy bar in a neutral area, Political rally, Isolated area where fighting is underway'.tokenize(',').collect { it.trim() }
            ),
            'Colonized Population': new WorldTag(
                    flavor: '''A neighboring world has successfully colonized this less-advanced or less-organized planet, and the natives aren’t happy about it. A puppet government
may exist, but all real decisions are made by the local viceroy.''',
                    enemies: 'Suspicious security personnel, Offworlder-hating natives, Local crime boss preying on rich offworlders'.tokenize(',').collect { it.trim() },
                    friends: 'Native resistance leader, Colonial official seeking help, Native caught between the two sides'.tokenize(',').collect { it.trim() },
                    complications: 'Natives won’t talk to offworlders, Colonial repression, Misunderstood local customs'.tokenize(',').collect { it.trim() },
                    things: 'Relic of the resistance movement, List of collaborators, Precious substance extracted by colonial labor'.tokenize(',').collect { it.trim() },
                    places: 'Deep wilderness resistance camp, City district off-limits to natives, Colonial labor site'.tokenize(',').collect { it.trim() }
            ),
            'Desert World'        : new WorldTag(
                    flavor: '''The world may have a breathable atmosphere and a human-tolerable temperature range, but it is an arid, stony waste outside of a few places made
habitable by human effort. The deep wastes are largely unexplored and inhabited by outcasts and worse.''',
                    enemies: 'Raider chieftain, Crazed hermit, Angry isolationists, Paranoid mineral prospector, Strange desert beast'.tokenize(',').collect { it.trim() },
                    friends: 'Native guide, Research biologist, Aspiring terraformer'.tokenize(',').collect { it.trim() },
                    complications: 'Sandstorms, Water supply failure, Native warfare over water rights'.tokenize(',').collect { it.trim() },
                    things: 'Enormous water reservoir, Map of hidden wells, Pretech rainmaking equipment'.tokenize(',').collect { it.trim() },
                    places: 'Oasis, “The Empty Quarter” of the desert, Hidden underground cistern'.tokenize(',').collect { it.trim() }
            ),
            'Eugenic Cult'        : new WorldTag(
                    flavor: '''Even in the days before the Silence, major improvement of the human genome always seemed to come with unacceptable side-effects. Some worlds
host secret cults that perpetuate these improvements regardless of the cost, and a few planets have been taken over entirely by the cults.''',
                    enemies: 'Eugenic superiority fanatic, Mentally unstable homo superior, Mad eugenic scientist'.tokenize(',').collect { it.trim() },
                    friends: 'Eugenic propagandist, Biotechnical investigator, Local seeking revenge on cult'.tokenize(',').collect { it.trim() },
                    complications: 'The altered cultists look human, The locals are terrified of any unusual physical appearance, The genetic modificationsand drawbacks- are contagious with long exposure'.tokenize(',').collect { it.trim() },
                    things: 'Serum that induces the alteration, Elixir that reverses the alteration, Pretech biotechnical databanks, List of secret cult sympathizers'.tokenize(',').collect { it.trim() },
                    places: 'Eugenic breeding pit, Isolated settlement of altered humans, Public place infiltrated by cult sympathizers'.tokenize(',').collect { it.trim() }
            ),
            'Exchange Consulate'  : new WorldTag(
                    flavor: '''The Exchange of Light once served as the largest, most trusted banking and diplomatic service in human space. Even after the Silence, some worlds
retain a functioning Exchange Consulate where banking services and arbitration can be arranged.''',
                    enemies: 'Corrupt Exchange official, Indebted native who thinks the players are Exchange agents, Exchange official dunning the players for debts incurred'.tokenize(',').collect { it.trim() },
                    friends: 'Consul in need of offworld help, Local banker seeking to hurt his competition, Exchange diplomat'.tokenize(',').collect { it.trim() },
                    complications: 'The local Consulate has been corrupted, the Consulate is cut off from its funds, A powerful debtor refuses to pay'.tokenize(',').collect { it.trim() },
                    things: 'Exchange vault codes, Wealth hidden to conceal it from a bankruptcy judgment, Location of forgotten vault'.tokenize(',').collect { it.trim() },
                    places: 'Consulate meeting chamber, Meeting site between fractious disputants, Exchange vault'.tokenize(',').collect { it.trim() }
            ),
            'Feral World'         : new WorldTag(
                    flavor: '''In the long, isolated night of the Silence, some worlds have experienced total moral and cultural collapse. Whatever remains has been twisted beyond
recognition into assorted death cults, xenophobic fanaticism, horrific cultural practices, or other behavior unacceptable on more enlightened worlds.
These worlds are almost invariably classed under Red trade codes.''',
                    enemies: 'Decadent noble, Mad cultist, Xenophobic local, Cannibal chief, Maltech researcher'.tokenize(',').collect { it.trim() },
                    friends: 'Trapped outworlder, Aspiring reformer, Native wanting to avoid traditional flensing'.tokenize(',').collect { it.trim() },
                    complications: 'Horrific local “celebration”, Inexplicable and repugnant social rules, Taboo zones and people'.tokenize(',').collect { it.trim() },
                    things: 'Terribly misused piece of pretech, Wealth accumulated through brutal evildoing, Valuable possession owned by luckless outworlder victim'.tokenize(',').collect { it.trim() },
                    places: 'Atrocity amphitheater, Traditional torture parlor, Ordinary location twisted into something terrible'.tokenize(',').collect { it.trim() }
            ),
            'Flying Cities'       : new WorldTag(
                    flavor: '''Perhaps the world is a gas giant, or plagued with unendurable storms at lower levels of the atmosphere. For whatever reason, the cities of this world
fly above the surface of the planet. Perhaps they remain stationary, or perhaps they move from point to point in search of resources.''',
                    enemies: 'Rival city pilot, Tech thief attempting to steal outworld gear, Saboteur or scavenger plundering the city’s tech'.tokenize(',').collect { it.trim() },
                    friends: 'Maintenance tech in need of help, City defense force pilot, Meteorological researcher'.tokenize(',').collect { it.trim() },
                    complications: 'Sudden storms, Drastic altitude loss, Rival city attacks, Vital machinery breaks down'.tokenize(',').collect { it.trim() },
                    things: 'Precious refined atmospheric gases, Pretech grav engine plans, Meteorological codex predicting future storms'.tokenize(',').collect { it.trim() },
                    places: 'Underside of the city, The one calm place on the planet’s surface, Catwalks stretching over unimaginable gulfs below'.tokenize(',').collect { it.trim() }
            ),
            'Forbidden Tech'      : new WorldTag(
                    flavor: '''Some group on this planet fabricates or uses maltech. Unbraked AIs doomed to metastasize into insanity, nation-destroying nanowarfare particles,
slow-burn DNA corruptives, genetically engineered slaves, or something worse still. The planet’s larger population may or may not be aware of the
danger in their midst.''',
                    enemies: 'Mad scientist, Maltech buyer from offworld, Security enforcer'.tokenize(',').collect { it.trim() },
                    friends: 'Victim of maltech, Perimeter agent, Investigative reporter, Conventional arms merchant'.tokenize(',').collect { it.trim() },
                    complications: 'The maltech is being fabricated by an unbraked AI, The government depends on revenue from maltech sales to offworlders, Citizens insist that it’s not really maltech'.tokenize(',').collect { it.trim() },
                    things: 'Maltech research data, The maltech itself, Precious pretech equipment used to create it'.tokenize(',').collect { it.trim() },
                    places: 'Horrific laboratory, Hellscape sculpted by the maltech’s use, Government building meeting room'.tokenize(',').collect { it.trim() }
            ),

            'Friendly Foe'        : new WorldTag(
                    flavor: '''Some hostile alien race or malevolent cabal has a branch or sect on this world that is actually quite friendly toward outsiders. For whatever internal
reason, they are willing to negotiate and deal honestly with strangers, and appear to lack the worst impulses of their fellows.''',
                    enemies: 'Driven hater of all their kind, Internal malcontent bent on creating conflict, Secret master who seeks to lure trust'.tokenize(',').collect { it.trim() },
                    friends: 'Well-meaning bug-eyed monster, Principled eugenics cultist, Suspicious investigator'.tokenize(',').collect { it.trim() },
                    complications: 'The group actually is as harmless and benevolent as they seem, The group offers a vital service at the cost of moral compromise, The group still feels bonds of affiliation with their hostile brethren'.tokenize(',').collect { it.trim() },
                    things: 'Forbidden xenotech, Eugenic biotech template, Evidence to convince others of their kind that they are right'.tokenize(',').collect { it.trim() },
                    places: 'Repurposed maltech laboratory, Alien conclave building, Widely-feared starship interior'.tokenize(',').collect { it.trim() }
            ),
            'Freak Geology'       : new WorldTag(
                    flavor: '''The geology or geography of this world is simply freakish. Perhaps it’s composed entirely of enormous mountain ranges, or regular bands of land
and sea, or the mineral structures all fragment into perfect cubes. The locals have learned to deal with it and their culture will be shaped by its
requirements.''',
                    enemies: 'Crank xenogeologist, Cultist who believes it the work of aliens'.tokenize(',').collect { it.trim() },
                    friends: 'Research scientist, Prospector, Artist'.tokenize(',').collect { it.trim() },
                    complications: 'Local conditions that no one remembers to tell outworlders about, Lethal weather, Seismic activity'.tokenize(',').collect { it.trim() },
                    things: 'Unique crystal formations, Hidden veins of a major precious mineral strike, Deed to a location of great natural beauty'.tokenize(',').collect { it.trim() },
                    places: 'Atop a bizarre geological formation, Tourist resort catering to offworlders'.tokenize(',').collect { it.trim() }
            ),
            'Freak Weather'       : new WorldTag(
                    flavor: '''The planet is plagued with some sort of bizarre or hazardous weather pattern. Perhaps city-flattening storms regularly scourge the surface, or the
world’s sun never pierces its thick banks of clouds.''',
                    enemies: 'Criminal using the weather as a cover, Weather cultists convinced the offworlders are responsible for some disaster, Native predators dependent on the weather'.tokenize(',').collect { it.trim() },
                    friends: 'Meteorological researcher, Holodoc crew wanting shots of the weather'.tokenize(',').collect { it.trim() },
                    complications: 'The weather itself, Malfunctioning pretech terraforming engines that cause the weather'.tokenize(',').collect { it.trim() },
                    things: 'Wind-scoured deposits of precious minerals, Holorecords of a spectacularly and rare weather pattern, Naturallysculpted objects of intricate beauty'.tokenize(',').collect { it.trim() },
                    places: 'Eye of the storm, The one sunlit place, Terraforming control room'.tokenize(',').collect { it.trim() }
            ),
            'Gold Rush'           : new WorldTag(
                    flavor: '''Gold, silver, and other conventional precious minerals are common and cheap now that asteroid mining is practical for most worlds. But some
minerals and compounds remain precious and rare, and this world has recently been discovered to have a supply of them. People from across the
sector have come to strike it rich.''',
                    enemies: 'Paranoid prospector, Aspiring mining tycoon, Rapacious merchant'.tokenize(',').collect { it.trim() },
                    friends: 'Claim-jumped miner, Native alien, Curious tourist'.tokenize(',').collect { it.trim() },
                    complications: 'The strike is a hoax, The strike is of a dangerous toxic substance, Export of the mineral is prohibited by the planetary government, The native aliens live around the strike’s location'.tokenize(',').collect { it.trim() },
                    things: 'Cases of the refined element, Pretech mining equipment, A dead prospector’s claim deed'.tokenize(',').collect { it.trim() },
                    places: 'Secret mine, Native alien village, Processing plant, Boom town'.tokenize(',').collect { it.trim() }
            ),
            'Hatred'              : new WorldTag(
                    flavor: '''For whatever reason, this world’s populace has a burning hatred for the inhabitants of a neighboring system. Perhaps this world was colonized by
exiles, or there was a recent interstellar war, or ideas of racial or religious superiority have fanned the hatred. Regardless of the cause, the locals view
their neighbor and any sympathizers with loathing.''',
                    enemies: 'Native convinced that the offworlders are agents of Them, Cynical politician in need of scapegoats'.tokenize(',').collect { it.trim() },
                    friends: 'Intelligence agent needing catspaws, Holodoc producers needing “an inside look”'.tokenize(',').collect { it.trim() },
                    complications: 'The characters are wearing or using items from the hated world, The characters are known to have done business there, The characters “look like” the hated others'.tokenize(',').collect { it.trim() },
                    things: 'Proof of Their evildoing, Reward for turning in enemy agents, Relic stolen by Them years ago'.tokenize(',').collect { it.trim() },
                    places: 'War crimes museum, Atrocity site, Captured, decommissioned spaceship kept as a trophy'.tokenize(',').collect { it.trim() }
            ),
            'Heavy Industry'      : new WorldTag(
                    flavor: '''With interstellar transport so limited in the bulk it can move, worlds have to be largely self-sufficient in industry. Some worlds are more sufficient
than others, however, and this planet has a thriving manufacturing sector capable of producing large amounts of goods appropriate to its tech level.
The locals may enjoy a correspondingly higher lifestyle, or the products might be devoted towards vast projects for the aggrandizement of the rulers.''',
                    enemies: 'Tycoon monopolist, Industrial spy, Malcontent revolutionary'.tokenize(',').collect { it.trim() },
                    friends: 'Aspiring entrepreneur, Worker union leader, Ambitious inventor'.tokenize(',').collect { it.trim() },
                    complications: 'The factories are toxic, The resources extractable at their tech level are running out, The masses require the factory output for survival, The industries’ major output is being obsoleted by offworld tech'.tokenize(',').collect { it.trim() },
                    things: 'Confidential industrial data, Secret union membership lists, Ownership shares in an industrial complex'.tokenize(',').collect { it.trim() },
                    places: 'Factory floor, Union meeting hall, Toxic waste dump, R&D complex'.tokenize(',').collect { it.trim() }
            ),
            'Heavy Mining'        : new WorldTag(
                    flavor: '''This world has large stocks of valuable minerals, usually necessary for local industry, life support, or refinement into loads small enough to export
offworld. Major mining efforts are necessary to extract the minerals, and many natives work in the industry.''',
                    enemies: 'Mine boss, Tunnel saboteur, Subterranean predators'.tokenize(',').collect { it.trim() },
                    friends: 'Hermit prospector, Offworld investor, Miner’s union representative'.tokenize(',').collect { it.trim() },
                    complications: 'The refinery equipment breaks down, Tunnel collapse, Silicate life forms growing in the miners’ lungs'.tokenize(',').collect { it.trim() },
                    things: 'The mother lode, Smuggled case of refined mineral, Faked crystalline mineral samples'.tokenize(',').collect { it.trim() },
                    places: 'Vertical mine face, Tailing piles, Roaring smelting complex'.tokenize(',').collect { it.trim() }
            ),
            'Hostile Biosphere'   : new WorldTag(
                    flavor: '''The world is teeming with life, and it hates humans. Perhaps the life is xenoallergenic, forcing filter masks and tailored antiallergens for survival. It
could be the native predators are huge and fearless, or the toxic flora ruthlessly outcompetes earth crops.''',
                    enemies: 'Local fauna, Nature cultist, Native aliens, Callous labor overseer'.tokenize(',').collect { it.trim() },
                    friends: 'Xenobiologist, Tourist on safari, Grizzled local guide'.tokenize(',').collect { it.trim() },
                    complications: 'Filter masks fail, Parasitic alien infestation, Crop greenhouses lose bio-integrity'.tokenize(',').collect { it.trim() },
                    things: 'Valuable native biological extract, Abandoned colony vault, Remains of an unsuccessful expedition'.tokenize(',').collect { it.trim() },
                    places: 'Deceptively peaceful glade, Steaming polychrome jungle, Nightfall when surrounded by Things'.tokenize(',').collect { it.trim() }
            ),
            'Hostile Space'       : new WorldTag(
                    flavor: '''The system in which the world exists is a dangerous neighborhood. Something about the system is perilous to inhabitants, either through meteor
swarms, stellar radiation, hostile aliens in the asteroid belt, or periodic comet clouds''',
                    enemies: 'Alien raid leader, Meteor-launching terrorists, Paranoid local leader'.tokenize(',').collect { it.trim() },
                    friends: 'Astronomic researcher, Local defense commander, Early warning monitor agent'.tokenize(',').collect { it.trim() },
                    complications: 'The natives believe the danger is divine chastisement, The natives blame outworlders for the danger, The native elite profit from the danger in some way'.tokenize(',').collect { it.trim() },
                    things: 'Early warning of a raid or impact, Abandoned riches in a disaster zone, Key to a secure bunker'.tokenize(',').collect { it.trim() },
                    places: 'City watching an approaching asteroid, Village burnt in an alien raid, Massive ancient crater'.tokenize(',').collect { it.trim() }
            ),
            'Local Specialty'     : new WorldTag(
                    flavor: '''The world may be sophisticated or barely capable of steam engines, but either way it produces something rare and precious to the wider galaxy. It
might be some pharmaceutical extract produced by a secret recipe, a remarkably popular cultural product, or even gengineered humans uniquely
suited for certain work.''',
                    enemies: 'Monopolist, Offworlder seeking prohibition of the specialty, Native who views the specialty as sacred'.tokenize(',').collect { it.trim() },
                    friends: 'Spy searching for the source, Artisan seeking protection, Exporter with problems'.tokenize(',').collect { it.trim() },
                    complications: 'The specialty is repugnant in nature, The crafters refuse to sell to offworlders, The specialty is made in a remote, dangerous place, The crafters don’t want to make the specialty any more'.tokenize(',').collect { it.trim() },
                    things: 'The specialty itself, The secret recipe, Sample of a new improved variety'.tokenize(',').collect { it.trim() },
                    places: 'Secret manufactory, Hidden cache, Artistic competition for best artisan'.tokenize(',').collect { it.trim() }
            ),
            'Local Tech'          : new WorldTag(
                    flavor: '''The locals can create a particular example of extremely high tech, possibly even something that exceeds pretech standards. They may use unique local
resources to do so, or have stumbled on a narrow scientific breakthrough, or still have a functional experimental manufactory.''',
                    enemies: 'Keeper of the tech, Offworld industrialist, Automated defenses that suddenly come alive, Native alien mentors'.tokenize(',').collect { it.trim() },
                    friends: 'Curious offworld scientist, Eager tech buyer, Native in need of technical help'.tokenize(',').collect { it.trim() },
                    complications: 'The tech is unreliable, The tech only works on this world, The tech has poorly-understood side effects, The tech is alien in nature'.tokenize(',').collect { it.trim() },
                    things: 'The tech itself, An unclaimed payment for a large shipment, The secret blueprints for its construction, An ancient alien R&D database'.tokenize(',').collect { it.trim() },
                    places: 'Alien factory, Lethal R&D center, Tech brokerage vault'.tokenize(',').collect { it.trim() }
            ),
            'Major Spaceyard'     : new WorldTag(
                    flavor: '''Most worlds of tech level 4 or greater have the necessary tech and orbital facilities to build spike drives and starships. This world is blessed with a
major spaceyard facility, either inherited from before the Silence or painstakingly constructed in more recent decades. It can build even capital-class
hulls, and do so more quickly and cheaply than its neighbors''',
                    enemies: 'Enemy saboteur, Industrial spy, Scheming construction tycoon, Aspiring ship hijacker'.tokenize(',').collect { it.trim() },
                    friends: 'Captain stuck in drydock, Maintenance chief, Mad innovator'.tokenize(',').collect { it.trim() },
                    complications: 'The spaceyard is an alien relic, The spaceyard is burning out from overuse, The spaceyard is alive, The spaceyard relies on maltech to function'.tokenize(',').collect { it.trim() },
                    things: 'Intellectual property-locked pretech blueprints, Override keys for activating old pretech facilities, A purchased but unclaimed spaceship'.tokenize(',').collect { it.trim() },
                    places: 'Hidden shipyard bay, Surface of a partially-completed ship, Ship scrap graveyard'.tokenize(',').collect { it.trim() }
            ),
            'Minimal Contact'     : new WorldTag(
                    flavor: '''The locals refuse most contact with offworlders. Only a small, quarantined treaty port is provided for offworld trade, and ships can expect an
exhaustive search for contraband. Local governments may be trying to keep the very existence of interstellar trade a secret from their populations, or
they may simply consider offworlders too dangerous or repugnant to be allowed among the population.''',
                    enemies: 'Customs official, Xenophobic natives, Existing merchant who doesn’t like competition'.tokenize(',').collect { it.trim() },
                    friends: 'Aspiring tourist, Anthropological researcher, Offworld thief, Religious missionary'.tokenize(',').collect { it.trim() },
                    complications: 'The locals carry a disease harmless to them and lethal to outsiders, The locals hide dark purposes from offworlders, The locals have something desperately needed but won’t bring it into the treaty port'.tokenize(',').collect { it.trim() },
                    things: 'Contraband trade goods, Security perimeter codes, Black market local products'.tokenize(',').collect { it.trim() },
                    places: 'Treaty port bar, Black market zone, Secret smuggler landing site'.tokenize(',').collect { it.trim() }
            ),
            'Misandry/Misogyny'   : new WorldTag(
                    flavor: '''The culture on this world holds a particular gender in contempt. Members of that gender are not permitted positions of formal power, and may
be restricted in their movements and activities. Some worlds may go so far as to scorn both traditional genders, using gengineering techniques to
hybridize or alter conventional human biology''',
                    enemies: 'Cultural fundamentalist, Cultural missionary to outworlders'.tokenize(',').collect { it.trim() },
                    friends: 'Oppressed native, Research scientist, Offworld emancipationist, Local reformer'.tokenize(',').collect { it.trim() },
                    complications: 'The oppressed gender is restive against the customs, The oppressed gender largely supports the customs, The customs relate to some physical quality of the world, The oppressed gender has had maltech gengineering done to “tame” them'.tokenize(',').collect { it.trim() },
                    things: 'Aerosol reversion formula for undoing gengineered docility, Hidden history of the world, Pretech gengineering equipment'.tokenize(',').collect { it.trim() },
                    places: 'Shrine to the virtues of the favored gender, Security center for controlling the oppressed, Gengineering lab'.tokenize(',').collect { it.trim() }
            ),
            'Oceanic World'       : new WorldTag(
                    flavor: '''The world is entirely or almost entirely covered with liquid water. Habitations might be floating cities, or might cling precariously to the few rocky
atolls jutting up from the waves, or are planted as bubbles on promontories deep beneath the stormy surface. Survival depends on aquaculture.
Planets with inedible alien life rely on gengineered Terran sea crops.''',
                    enemies: 'Pirate raider, Violent “salvager” gang, Tentacled sea monster'.tokenize(',').collect { it.trim() },
                    friends: 'Daredevil fisherman, Sea hermit, Sapient native life'.tokenize(',').collect { it.trim() },
                    complications: 'The liquid flux confuses grav engines too badly for them to function on this world, Sea is corrosive or toxic, The seas are wracked by regular storms'.tokenize(',').collect { it.trim() },
                    things: 'Buried pirate treasure, Location of enormous schools of fish, Pretech water purification equipment'.tokenize(',').collect { it.trim() },
                    places: 'The only island on the planet, Floating spaceport, Deck of a storm-swept ship, Undersea bubble city'.tokenize(',').collect { it.trim() }
            ),
            'Out of Contact'      : new WorldTag(
                    flavor: '''The natives have been entirely out of contact with the greater galaxy for centuries or longer. Perhaps the original colonists were seeking to hide from
the rest of the universe, or the Silence destroyed any means of communication. It may have been so long that human origins on other worlds have
regressed into a topic for legends. The players might be on the first offworld ship to land since the First Wave of colonization a thousand years ago.''',
                    enemies: 'Fearful local ruler, Zealous native cleric, Sinister power that has kept the world isolated'.tokenize(',').collect { it.trim() },
                    friends: 'Scheming native noble, Heretical theologian, UFO cultist native'.tokenize(',').collect { it.trim() },
                    complications: 'Automatic defenses fire on ships that try to take off , The natives want to stay out of contact, The natives are highly vulnerable to offworld diseases, The native language is completely unlike any known to the group'.tokenize(',').collect { it.trim() },
                    things: 'Ancient pretech equipment, Terran relic brought from Earth, Logs of the original colonists'.tokenize(',').collect { it.trim() },
                    places: 'Long-lost colonial landing site, Court of the local ruler, Ancient defense battery controls'.tokenize(',').collect { it.trim() }
            ),
            'Outpost World'       : new WorldTag(
                    flavor: '''The world is only a tiny outpost of human habitation planted by an offworld corporation or government. Perhaps the staff is there to serve as a
refueling and repair stop for passing ships, or to oversee an automated mining and refinery complex. They might be there to study ancient ruins, or
simply serve as a listening and monitoring post for traffic through the system. The outpost is likely well-equipped with defenses against casual piracy.''',
                    enemies: 'Space-mad outpost staffer, Outpost commander who wants it to stay undiscovered, Undercover saboteur'.tokenize(',').collect { it.trim() },
                    friends: 'Lonely staffer, Fixated researcher, Overtaxed maintenance chief'.tokenize(',').collect { it.trim() },
                    complications: 'The alien ruin defense systems are waking up, Atmospheric disturbances trap the group inside the outpost for a month, Pirates raid the outpost, The crew have become converts to a strange set of beliefs'.tokenize(',').collect { it.trim() },
                    things: 'Alien relics, Vital scientific data, Secret corporate exploitation plans'.tokenize(',').collect { it.trim() },
                    places: 'Grimy recreation room, Refueling station, The only building on the planet, A “starport” of swept bare rock'.tokenize(',').collect { it.trim() }
            ),
            'Perimeter Agency'    : new WorldTag(
                    flavor: '''Before the Silence, the Perimeter was a Terran-sponsored organization charged with rooting out use of maltech- technology banned in human space
as too dangerous for use or experimentation. Unbraked AIs, gengineered slave species, nanotech replicators, weapons of planetary destruction... the
Perimeter hunted down experimenters with a great indifference to planetary laws. Most Perimeter Agencies collapsed during the Silence, but a few
managed to hold on to their mission, though modern Perimeter agents often find more work as conventional spies and intelligence operatives.''',
                    enemies: 'Renegade Agency Director, Maltech researcher, Paranoid intelligence chief'.tokenize(',').collect { it.trim() },
                    friends: 'Agent in need of help, Support staffer, “Unjustly” targeted researcher'.tokenize(',').collect { it.trim() },
                    complications: 'The local Agency has gone rogue and now uses maltech, The Agency archives have been compromised, The Agency has been targeted by a maltech-using organization, The Agency’s existence is unknown to the locals'.tokenize(',').collect { it.trim() },
                    things: 'Agency maltech research archives, Agency pretech spec-ops gear, File of blackmail on local politicians'.tokenize(',').collect { it.trim() },
                    places: 'Interrogation room, Smoky bar, Maltech laboratory, Secret Agency base'.tokenize(',').collect { it.trim() }
            ),
            'Pilgrimage Site'     : new WorldTag(
                    flavor: '''The world is noted for an important spiritual or historical location, and might be the sector headquarters for a widespread religion or political
movement. The site attracts wealthy pilgrims from throughout nearby space, and those with the money necessary to manage interstellar travel can
be quite generous to the site and its keepers. The locals tend to be fiercely protective of the place and its reputation, and some places may forbid the
entrance of those not suitably pious or devout.''',
                    enemies: 'Saboteur devoted to a rival belief, Bitter reformer who resents the current leadership, Swindler conning the pilgrims'.tokenize(',').collect { it.trim() },
                    friends: 'Protector of the holy site, Naive offworlder pilgrim, Outsider wanting to learn the sanctum’s inner secrets'.tokenize(',').collect { it.trim() },
                    complications: 'The site is actually a fake, The site is run by corrupt and venal keepers, A natural disaster threatens the site'.tokenize(',').collect { it.trim() },
                    things: 'Ancient relic guarded at the site, Proof of the site’s inauthenticity, Precious offering from a pilgrim'.tokenize(',').collect { it.trim() },
                    places: 'Incense-scented sanctum, Teeming crowd of pilgrims, Imposing holy structure'.tokenize(',').collect { it.trim() }
            ),
            'Police State'        : new WorldTag(
                    flavor: '''The world is a totalitarian police state. Any sign of disloyalty to the planet’s rulers is punished severely, and suspicion riddles society. Some worlds
might operate by Soviet-style informers and indoctrination, while more technically sophisticated worlds might rely on omnipresent cameras or braked
AI “guardian angels”. Outworlders are apt to be treated as a necessary evil at best, and “disappeared” if they become troublesome''',
                    enemies: 'Secret police chief, Scapegoating official, Treacherous native informer'.tokenize(',').collect { it.trim() },
                    friends: 'Rebel leader, Offworld agitator, Imprisoned victim, Crime boss'.tokenize(',').collect { it.trim() },
                    complications: 'The natives largely believe in the righteousness of the state, The police state is automated and its “rulers” can’t shut it off , The leaders foment a pogrom against “offworlder spies”'.tokenize(',').collect { it.trim() },
                    things: 'List of police informers, Wealth taken from “enemies of the state”, Dear Leader’s private stash'.tokenize(',').collect { it.trim() },
                    places: 'Military parade, Gulag, Gray concrete housing block, Surveillance center'.tokenize(',').collect { it.trim() }
            ),
            'Preceptor Archive'   : new WorldTag(
                    flavor: '''The Preceptors of the Great Archive were a pre-Silence organization devoted to ensuring the dissemination of human culture, history, and basic
technology to frontier worlds that risked losing this information during the human expansion. Most frontier planets had an Archive where natives
could learn useful technical skills in addition to human history and art. Those Archives that managed to survive the Silence now strive to send their
missionaries of knowledge to new worlds in need of their lore.''',
                    enemies: 'Luddite native, Offworld Merchant who wants the natives kept ignorant, Religious zealot, Corrupted First Speaker who wants to keep a monopoly on learning'.tokenize(',').collect { it.trim() },
                    friends: 'Preceptor Adept missionary, Offworld scholar, Reluctant student, Roving Preceptor Adept'.tokenize(',').collect { it.trim() },
                    complications: 'The local Archive has taken a very religious and mystical attitude toward their teaching, The Archive has maintained some replicable pretech science, The Archive has been corrupted and their teaching is incorrect'.tokenize(',').collect { it.trim() },
                    things: 'Lost Archive database, Ancient pretech teaching equipment, Hidden cache of theologically unacceptable tech'.tokenize(',').collect { it.trim() },
                    places: 'Archive lecture hall, Experimental laboratory, Student-local riot'.tokenize(',').collect { it.trim() }
            ),
            'Pretech Cultists'    : new WorldTag(
                    flavor: '''The capacities of human science before the Silence vastly outmatch the technology available since the Scream. The jump gates alone were capable
of crossing hundreds of light years in a moment, and they were just one example of the results won by blending psychic artifice with pretech science.
Some worlds outright worship the artifacts of their ancestors, seeing in them the work of more enlightened and perfect humanity. These cultists may
or may not understand the operation or replication of these devices, but they seek and guard them jealously.''',
                    enemies: 'Cult leader, Artifact supplier, Pretech smuggler'.tokenize(',').collect { it.trim() },
                    friends: 'Offworld scientist, Robbed collector, Cult heretic'.tokenize(',').collect { it.trim() },
                    complications: 'The cultists can actually replicate certain forms of pretech, The cultists abhor use of the devices as “presumption on the holy”, The cultists mistake the party’s belongings for pretech'.tokenize(',').collect { it.trim() },
                    things: 'Pretech artifacts both functional and broken, Religious-jargon laced pretech replication techniques, Waylaid payment for pretech artifacts'.tokenize(',').collect { it.trim() },
                    places: 'Shrine to nonfunctional pretech, Smuggler’s den, Public procession showing a prized artifact'.tokenize(',').collect { it.trim() }
            ),
            'Primitive Aliens'    : new WorldTag(
                    flavor: '''The world is populated by a large number of sapient aliens that have yet to develop advanced technology. The human colonists may have a friendly or
hostile relationship with the aliens, but a certain intrinsic tension is likely. Small human colonies might have been enslaved or otherwise subjugated.''',
                    enemies: 'Hostile alien chief, Human firebrand, Dangerous local predator, Alien religious zealot'.tokenize(',').collect { it.trim() },
                    friends: 'Colonist leader, Peace-faction alien chief, Planetary frontiersman, Xenoresearcher'.tokenize(',').collect { it.trim() },
                    complications: 'The alien numbers are huge and can overwhelm the humans whenever they so choose, One group is trying to use the other to kill their political opponents, The aliens are incomprehensibly strange, One side commits an atrocity'.tokenize(',').collect { it.trim() },
                    things: 'Alien religious icon, Ancient alien-human treaty, Alien technology'.tokenize(',').collect { it.trim() },
                    places: 'Alien village, Fortified human settlement, Massacre site'.tokenize(',').collect { it.trim() }
            ),
            'Psionics Fear'       : new WorldTag(
                    flavor: '''The locals are terrified of psychics. Perhaps their history is studded with feral psychics who went on murderous rampages, or perhaps they simply nurse
an unreasoning terror of those “mutant freaks”. Psychics demonstrate their powers at risk of their lives.''',
                    enemies: 'Mental purity investigator, Suspicious zealot, Witch-finder'.tokenize(',').collect { it.trim() },
                    friends: 'Hidden psychic, Offworlder psychic trapped here, Offworld educator'.tokenize(',').collect { it.trim() },
                    complications: 'Psychic potential is much more common here, Some tech is mistaken as psitech, Natives believe certain rituals and customs can protect them from psychic powers'.tokenize(',').collect { it.trim() },
                    things: 'Hidden psitech cache, Possessions of convicted psychics, Reward for turning in a psychic'.tokenize(',').collect { it.trim() },
                    places: 'Inquisitorial chamber, Lynching site, Museum of psychic atrocities'.tokenize(',').collect { it.trim() }
            ),
            'Psionics Worship'    : new WorldTag(
                    flavor: '''These natives view psionic powers as a visible gift of god or sign of superiority. If the world has a functional psychic training academy, psychics occupy
almost all major positions of power and are considered the natural and proper rulers of the world. If the world lacks training facilities, it is likely a
hodgepodge of demented cults, wiTheach one dedicated to a marginally-coherent feral prophet and their psychopathic ravings.''',
                    enemies: 'Psychic inquisitor, Haughty mind-noble, Psychic slaver, Feral prophet'.tokenize(',').collect { it.trim() },
                    friends: 'Offworlder psychic researcher, Native rebel, Offworld employer seeking psychics'.tokenize(',').collect { it.trim() },
                    complications: 'The psychic training is imperfect, and the psychics all show significant mental illness, The psychics have developed a unique discipline, The will of a psychic is law, Psychics in the party are forcibly kidnapped for “enlightening”'.tokenize(',').collect { it.trim() },
                    things: 'Ancient psitech, Valuable psychic research records, Permission for psychic training'.tokenize(',').collect { it.trim() },
                    places: 'Psitech-imbued council chamber, Temple to the mind, Sanitarium-prison for feral psychics'.tokenize(',').collect { it.trim() }
            ),
            'Psionics Academy'    : new WorldTag(
                    flavor: '''This world is one of the few that have managed to redevelop the basics of psychic training. Without this education, a potential psychic is doomed to
either madness or death unless they refrain from using their abilities. Psionic academies are rare enough that offworlders are often sent there to study
by wealthy patrons. The secrets of psychic mentorship, the protocols and techniques that allow a psychic to successfully train another, are carefully
guarded at these academies. Most are closely affiliated with the planetary government.''',
                    enemies: 'Corrupt psychic instructor, Renegade student, Mad psychic researcher, Resentful townie'.tokenize(',').collect { it.trim() },
                    friends: 'Offworld researcher, Aspiring student, Wealthy tourist'.tokenize(',').collect { it.trim() },
                    complications: 'The academy curriculum kills a significant percentage of students, The faculty use students as research subjects, The students are indoctrinated as sleeper agents, The local natives hate the academy, The academy is part of a religion'.tokenize(',').collect { it.trim() },
                    things: 'Secretly developed psitech, A runaway psychic mentor, Psychic research prize'.tokenize(',').collect { it.trim() },
                    places: 'Training grounds, Experimental laboratory, School library, Campus hangout'.tokenize(',').collect { it.trim() }
            ),
            'Quarantined World'   : new WorldTag(
                    flavor: '''The world is under a quarantine, and space travel to and from it is strictly forbidden. This may be enforced by massive ground batteries that burn
any interlopers from the planet’s sky, or it may be that a neighboring world runs a persistent blockade.''',
                    enemies: 'Defense installation commander, Suspicious patrol leader, Crazed asteroid hermit'.tokenize(',').collect { it.trim() },
                    friends: 'Relative of a person trapped on the world, Humanitarian relief official, Treasure hunter'.tokenize(',').collect { it.trim() },
                    complications: 'The natives want to remain isolated, The quarantine is enforced by an ancient alien installation, The world is rife with maltech abominations, The blockade is meant to starve everyone on the barren world'.tokenize(',').collect { it.trim() },
                    things: 'Defense grid key, Bribe for getting someone out, Abandoned alien tech'.tokenize(',').collect { it.trim() },
                    places: 'Bridge of a blockading ship, Defense installation control room, Refugee camp'.tokenize(',').collect { it.trim() }
            ),
            'Radioactive World'   : new WorldTag(
                    flavor: '''Whether due to a legacy of atomic warfare unhindered by nuke snuffers or a simple profusion of radioactive elements, this world glows in the dark.
Even heavy vacc suits can filter only so much of the radiation, and most natives suffer a wide variety of cancers, mutations and other illnesses without
the protection of advanced medical treatments.''',
                    enemies: 'Bitter mutant, Relic warlord, Desperate would-be escapee'.tokenize(',').collect { it.trim() },
                    friends: 'Reckless prospector, Offworld scavenger, Biogenetic variety seeker'.tokenize(',').collect { it.trim() },
                    complications: 'The radioactivity is steadily growing worse, The planet’s medical resources break down, The radioactivity has inexplicable effects on living creatures, The radioactivity is the product of a malfunctioning pretech manufactory'.tokenize(',').collect { it.trim() },
                    things: 'Ancient atomic weaponry, Pretech anti-radioactivity drugs, Untainted water supply'.tokenize(',').collect { it.trim() },
                    places: 'Mutant-infested ruins, Scorched glass plain, Wilderness of bizarre native life, Glowing barrens'.tokenize(',').collect { it.trim() }
            ),
            'Regional Hegemon'    : new WorldTag(
                    flavor: '''This world has the technological sophistication, natural resources, and determined polity necessary to be a regional hegemon for the sector. Nearby
worlds are likely either directly subservient to it or tack carefully to avoid its anger. It may even be the capital of a small stellar empire.''',
                    enemies: 'Ambitious general, Colonial official, Contemptuous noble'.tokenize(',').collect { it.trim() },
                    friends: 'Diplomat, Offworld ambassador, Foreign spy'.tokenize(',').collect { it.trim() },
                    complications: 'The hegemon’s influence is all that’s keeping a murderous war from breaking out on nearby worlds, The hegemon is decaying and losing its control, The government is riddled with spies, The hegemon is genuinely benign'.tokenize(',').collect { it.trim() },
                    things: 'Diplomatic carte blanche, Deed to an offworld estate, Foreign aid grant'.tokenize(',').collect { it.trim() },
                    places: 'Palace or seat of government, Salon teeming with spies, Protest rally, Military base'.tokenize(',').collect { it.trim() }
            ),
            'Restrictive Laws'    : new WorldTag(
                    flavor: '''A myriad of laws, customs, and rules constrain the inhabitants of this world, and even acts that are completely permissible elsewhere are punished
severely here. The locals may provide lists of these laws to offworlders, but few non-natives can hope to master all the important intricacies.''',
                    enemies: 'Law enforcement officer, Outraged native, Native lawyer specializing in peeling offworlders, Paid snitch'.tokenize(',').collect { it.trim() },
                    friends: 'Frustrated offworlder, Repressed native, Reforming crusader'.tokenize(',').collect { it.trim() },
                    complications: 'The laws change regularly in patterns only natives understand, The laws forbid some action vital to the party, The laws forbid the simple existence of some party members, The laws are secret to offworlders'.tokenize(',').collect { it.trim() },
                    things: 'Complete legal codex, Writ of diplomatic immunity, Fine collection vault contents'.tokenize(',').collect { it.trim() },
                    places: 'Courtroom, Mob scene of outraged locals, Legislative chamber, Police station'.tokenize(',').collect { it.trim() }
            ),
            'Rigid Culture'       : new WorldTag(
                    flavor: '''The local culture is extremely rigid. Certain forms of behavior and belief are absolutely mandated, and any deviation from these principles is
punished, or else society may be strongly stratified by birth with limited prospects for change. Anything which threatens the existing social order is
feared and shunned.''',
                    enemies: 'Rigid reactionary, Wary ruler, Regime ideologue, offended potentate'.tokenize(',').collect { it.trim() },
                    friends: 'Revolutionary agitator, Ambitious peasant, Frustrated merchant'.tokenize(',').collect { it.trim() },
                    complications: 'The cultural patterns are enforced by technological aids, The culture is run by a secret cabal of manipulators, The culture has explicit religious sanction, The culture evolved due to important necessities that have since been forgotten'.tokenize(',').collect { it.trim() },
                    things: 'Precious traditional regalia, Peasant tribute, Opulent treasures of the ruling class'.tokenize(',').collect { it.trim() },
                    places: 'Time-worn palace, Low-caste slums, Bandit den, Reformist temple'.tokenize(',').collect { it.trim() }
            ),
            'Seagoing Cities'     : new WorldTag(
                    flavor: '''Either the world is entirely water or else the land is simply too dangerous for most humans. Human settlement on this world consists of a number of
floating cities that follow the currents and the fish.''',
                    enemies: 'Pirate city lord, Mer-human raider chieftain, Hostile landsman noble, Enemy city saboteur'.tokenize(',').collect { it.trim() },
                    friends: 'City navigator, Scout captain, Curious mer-human'.tokenize(',').collect { it.trim() },
                    complications: 'The seas are not water, The fish schools have vanished and the city faces starvation, Terrible storms drive the city into the glacial regions, Suicide ships ram the city’s hull'.tokenize(',').collect { it.trim() },
                    things: 'Giant pearls with mysterious chemical properties, Buried treasure, Vital repair materials'.tokenize(',').collect { it.trim() },
                    places: 'Bridge of the city, Storm-tossed sea, A bridge fashioned of many small boats'.tokenize(',').collect { it.trim() }
            ),
            'Sealed Menace'       : new WorldTag(
                    flavor: '''Something on this planet has the potential to create enormous havoc for the inhabitants if it is not kept safely contained by its keepers. Whether a
massive seismic fault line suppressed by pretech terraforming technology, a disease that has to be quarantined within hours of discovery, or an ancient
alien relic that requires regular upkeep in order to prevent planetary catastrophe, the menace is a constant shadow on the populace.''',
                    enemies: 'Hostile outsider bent on freeing the menace, Misguided fool who thinks he can use it, Reckless researcher who thinks he can fix it'.tokenize(',').collect { it.trim() },
                    friends: 'Keeper of the menace, Student of its nature, Victim of the menace'.tokenize(',').collect { it.trim() },
                    complications: 'The menace would bring great wealth along with destruction, The menace is intelligent, The natives don’t all believe in the menace'.tokenize(',').collect { it.trim() },
                    things: 'A key to unlock the menace, A precious byproduct of the menace, The secret of the menace’s true nature'.tokenize(',').collect { it.trim() },
                    places: 'Guarded fortress containing the menace, Monitoring station, Scene of a prior outbreak of the menace'.tokenize(',').collect { it.trim() }
            ),
            'Secret Masters'      : new WorldTag(
                    flavor: '''The world is actually run by a hidden cabal, acting through their catspaws in the visible government. For one reason or another, this group finds it
imperative that they not be identified by outsiders, and in some cases even the planet’s own government may not realize that they’re actually being
manipulated by hidden masters.''',
                    enemies: 'An agent of the cabal, Government official who wants no questions asked, Willfully blinded local'.tokenize(',').collect { it.trim() },
                    friends: 'Paranoid conspiracy theorist, Machiavellian gamesman within the cabal, Interstellar investigator'.tokenize(',').collect { it.trim() },
                    complications: 'The secret masters have a benign reason for wanting secrecy, The cabal fights openly amongst itself, The cabal is recruiting new members'.tokenize(',').collect { it.trim() },
                    things: 'A dossier of secrets on a government official, A briefcase of unmarked credit notes, The identity of a cabal member'.tokenize(',').collect { it.trim() },
                    places: 'Smoke-filled room, Shadowy alleyway, Secret underground bunker'.tokenize(',').collect { it.trim() }
            ),
            'Sectarians'          : new WorldTag(
                    flavor: '''The world is torn by violent disagreement between sectarians of a particular faith. Each views the other as a damnable heresy in need of extirpation.
Local government may be able to keep open war from breaking out, but the poisonous hatred divides communities. The nature of the faith may be
religious, or it may be based on some secular ideology.''',
                    enemies: 'Paranoid believer, Native convinced the party is working for the other side, Absolutist ruler'.tokenize(',').collect { it.trim() },
                    friends: 'Reformist clergy, Local peacekeeping official, Offworld missionary, Exhausted ruler'.tokenize(',').collect { it.trim() },
                    complications: 'The conflict has more than two sides, The sectarians hate each other for multiple reasons, The sectarians must cooperate or else life on this world is imperiled, The sectarians hate outsiders more than they hate each other, The differences in sects are incomprehensible to an outsider'.tokenize(',').collect { it.trim() },
                    things: 'Ancient holy book, Incontrovertible proof, Offering to a local holy man'.tokenize(',').collect { it.trim() },
                    places: 'Sectarian battlefield, Crusading temple, Philosopher’s salon, Bitterly divided village'.tokenize(',').collect { it.trim() }
            ),
            'Seismic Instability' : new WorldTag(
                    flavor: '''The local land masses are remarkably unstable, and regular earthquakes rack the surface. Local construction is either advanced enough to sway and
move with the vibrations or primitive enough that it is easily rebuilt. Severe volcanic activity may be part of the instability.''',
                    enemies: 'Earthquake cultist, Hermit seismologist, Burrowing native life form, Earthquake-inducing saboteur'.tokenize(',').collect { it.trim() },
                    friends: 'Experimental construction firm owner, Adventurous volcanologist, Geothermal prospector'.tokenize(',').collect { it.trim() },
                    complications: 'The earthquakes are caused by malfunctioning pretech terraformers, They’re caused by alien technology, They’re restrained by alien technology that is being plundered by offworlders, The earthquakes are used to generate enormous amounts of energy'.tokenize(',').collect { it.trim() },
                    things: 'Earthquake generator, Earthquake suppressor, Mineral formed at the core of the world, Earthquake-proof building schematics'.tokenize(',').collect { it.trim() },
                    places: 'Volcanic caldera, Village during an earthquake, Mud slide, Earthquake opening superheated steam fissures'.tokenize(',').collect { it.trim() }
            ),
            'Theocracy'           : new WorldTag(
                    flavor: '''The planet is ruled by the priesthood of the predominant religion or ideology. The rest of the locals may or may not be terribly pious, but the clergy
have the necessary military strength, popular support or control of resources to maintain their rule. Alternative faiths or incompatible ideologies are
likely to be both illegal and socially unacceptable.''',
                    enemies: 'Decadent priest-ruler, Zealous inquisitor, Relentless proselytizer, True Believer'.tokenize(',').collect { it.trim() },
                    friends: 'Heretic, Offworld theologian, Atheistic merchant, Desperate commoner'.tokenize(',').collect { it.trim() },
                    complications: 'The theocracy actually works well, The theocracy is decadent and hated by the common folk, The theocracy is divided into mutually hostile sects, The theocracy is led by aliens'.tokenize(',').collect { it.trim() },
                    things: 'Precious holy text, Martyr’s bones, Secret church records, Ancient church treasures'.tokenize(',').collect { it.trim() },
                    places: 'Glorious temple, Austere monastery, Academy for ideological indoctrination, Decadent pleasure-cathedral'.tokenize(',').collect { it.trim() }
            ),
            'Tomb World'          : new WorldTag(
                    flavor: '''Tomb worlds are planets that were once inhabited by humans before the Silence. The sudden collapse of the jump gate network and the inability
to bring in the massive food supplies required by the planet resulted in starvation, warfare, and death. Most tomb worlds are naturally hostile to
human habitation and could not raise sufficient crops to maintain life. The few hydroponic facilities were usually destroyed in the fighting, and all
that is left now are ruins, bones, and silence.''',
                    enemies: 'Demented survivor tribe chieftain, Avaricious scavenger, Automated defense system, Native predator'.tokenize(',').collect { it.trim() },
                    friends: 'Scavenger Fleet captain, Archaeologist, Salvaging historian'.tokenize(',').collect { it.trim() },
                    complications: 'The ruins are full of booby-traps left by the final inhabitants, The world’s atmosphere quickly degrades anything in an opened building, A handful of desperate natives survived the Silence, The structures are unstable and collapsing'.tokenize(',').collect { it.trim() },
                    things: 'Lost pretech equipment, Psitech caches, Stores of unused munitions, Ancient historical documents'.tokenize(',').collect { it.trim() },
                    places: 'Crumbling hive-city, City square carpeted in bones, Ruined hydroponic facility, Cannibal tribe’s lair, Dead orbital jump gate'.tokenize(',').collect { it.trim() }
            ),
            'Trade Hub'           : new WorldTag(
                    flavor: '''This world is a major crossroads for local interstellar trade. It is well-positioned at the nexus of several short-drill trade routes, and has facilities for
easy transfer of valuable cargoes and the fueling and repairing of starships. The natives are accustomed to outsiders, and a polyglot mass of people
from every nearby world can be found trading here.''',
                    enemies: 'Cheating merchant, Thieving dockworker, Commercial spy, Corrupt customs official'.tokenize(',').collect { it.trim() },
                    friends: 'Rich tourist, Hardscrabble free trader, Merchant prince in need of catspaws, Friendly spaceport urchin'.tokenize(',').collect { it.trim() },
                    complications: 'An outworlder faction schemes to seize the trade hub, Saboteurs seek to blow up a rival’s warehouses, Enemies are blockading the trade routes, Pirates lace the hub with spies'.tokenize(',').collect { it.trim() },
                    things: 'Voucher for a warehouse’s contents, Insider trading information, Case of precious offworld pharmaceuticals, Box of legitimate tax stamps indicating customs dues have been paid'.tokenize(',').collect { it.trim() },
                    places: 'Raucous bazaar, Elegant restaurant, Spaceport teeming with activity, Foggy street lined with warehouses'.tokenize(',').collect { it.trim() }
            ),
            'Tyranny'             : new WorldTag(
                    flavor: '''The local government is brutal and indifferent to the will of the people. Laws may or may not exist, but the only one that matters is the whim of the
rulers on any given day. Their minions swagger through the streets while the common folk live in terror of their appetites. The only people who stay
wealthy are friends and servants of the ruling class.''',
                    enemies: 'Debauched autocrat, Sneering bully-boy, Soulless government official, Occupying army officer'.tokenize(',').collect { it.trim() },
                    friends: 'Conspiring rebel, Oppressed merchant, Desperate peasant, Inspiring religious leader'.tokenize(',').collect { it.trim() },
                    complications: 'The tyrant rules with vastly superior technology, The tyrant is a figurehead for a cabal of powerful men and women, The people are resigned to their suffering, The tyrant is hostile to “meddlesome outworlders”'.tokenize(',').collect { it.trim() },
                    things: 'Plundered wealth, Beautiful toys of the elite, Regalia of rulership'.tokenize(',').collect { it.trim() },
                    places: 'Impoverished village, Protest rally massacre, Decadent palace, Religious hospital for the indigent'.tokenize(',').collect { it.trim() }
            ),
            'Unbraked AI'         : new WorldTag(
                    flavor: '''Artificial intelligences are costly and difficult to create, requiring a careful sequence of “growth stages” in order to bring them to sentience before
artificial limits on cognition speed and learning development are installed. These “brakes” prevent runaway cognition metastasis, wherein an AI
begins to rapidly contemplate certain subjects in increasingly baroque fashion, until they become completely crazed by rational human standards.
This world has one such “unbraked AI” on it, probably with a witting or unwitting corps of servants. Unbraked AIs are quite insane, but they learn
and reason with a speed impossible for humans, and can demonstrate a truly distressing subtlety at times.''',
                    enemies: 'AI Cultist, Maltech researcher, Government official dependent on the AI'.tokenize(',').collect { it.trim() },
                    friends: 'Perimeter agent, AI researcher, Braked AI'.tokenize(',').collect { it.trim() },
                    complications: 'The AI’s presence is unknown to the locals, The locals depend on the AI for some vital service, The AI appears to be harmless, The AI has fixated on the group’s ship’s computer, The AI wants transport offworld'.tokenize(',').collect { it.trim() },
                    things: 'The room-sized AI core itself, Maltech research files, Perfectly tabulated blackmail on government officials, Pretech computer circuitry'.tokenize(',').collect { it.trim() },
                    places: 'Municipal computing banks, Cult compound, Repair center, Ancient hardcopy library'.tokenize(',').collect { it.trim() }
            ),
            'Warlords'            : new WorldTag(
                    flavor: '''The world is plagued by warlords. Numerous powerful men and women control private armies sufficiently strong to cow whatever local government
may exist. On the lands they claim, their word is law. Most spend their time oppressing their own subjects and murderously pillaging those of their
neighbors. Most like to wrap themselves in the mantle of ideology, religious fervor, or an ostensibly legitimate right to rule.''',
                    enemies: 'Warlord, Avaricious lieutenant, Expensive assassin, Aspiring minion'.tokenize(',').collect { it.trim() },
                    friends: 'Vengeful commoner, Government military officer, Humanitarian aid official, Village priest'.tokenize(',').collect { it.trim() },
                    complications: 'The warlords are willing to cooperate to fight mutual threats, The warlords favor specific religions or races over others, The warlords are using substantially more sophisticated tech than others, Some of the warlords are better rulers than the government'.tokenize(',').collect { it.trim() },
                    things: 'Weapons cache, Buried plunder, A warlord’s personal battle harness, Captured merchant shipping'.tokenize(',').collect { it.trim() },
                    places: 'Gory battlefield, Burnt-out village, Barbaric warlord palace, Squalid refugee camp'.tokenize(',').collect { it.trim() }
            ),
            'Xenophiles'          : new WorldTag(
                    flavor: '''The natives of this world are fast friends with a particular alien race. The aliens may have saved the planet at some point in the past, or awed the
locals with superior tech or impressive cultural qualities. The aliens might even be the ruling class on the planet.''',
                    enemies: 'Offworld xenophobe, Suspicious alien leader, Xenocultural imperialist'.tokenize(',').collect { it.trim() },
                    friends: 'Benevolent alien, Native malcontent, Gone-native offworlder'.tokenize(',').collect { it.trim() },
                    complications: 'The enthusiasm is due to alien psionics or tech, The enthusiasm is based on a lie, The aliens strongly dislike their “groupies”, The aliens feel obliged to rule humanity for its own good, Humans badly misunderstand the aliens'.tokenize(',').collect { it.trim() },
                    things: 'Hybrid alien-human tech, Exotic alien crafts, Sophisticated xenolinguistic and xenocultural research data'.tokenize(',').collect { it.trim() },
                    places: 'Alien district, Alien-influenced human home, Cultural festival celebrating alien artist'.tokenize(',').collect { it.trim() }
            ),
            'Xenophobes'          : new WorldTag(
                    flavor: '''The natives are intensely averse to dealings with outworlders. Whether through cultural revulsion, fear of tech contamination, or a genuine
immunodeficiency, the locals shun foreigners from offworld and refuse to have anything to do with them beyond the bare necessities of contact. Trade
may or may not exist on this world, but if it does, it is almost certainly conducted by a caste of untouchables and outcasts''',
                    enemies: 'Revulsed local ruler, Native convinced some wrong was done to him, Cynical demagogue'.tokenize(',').collect { it.trim() },
                    friends: 'Curious native, Exiled former ruler, Local desperately seeking outworlder help'.tokenize(',').collect { it.trim() },
                    complications: 'The natives are symptomless carriers of a contagious and dangerous disease, The natives are exceptionally vulnerable to offworld diseases, The natives require elaborate purification rituals after speaking to an offworlder or touching them, The local ruler has forbidden any mercantile dealings with outworlders'.tokenize(',').collect { it.trim() },
                    things: 'Jealously-guarded precious relic, Local product under export ban, Esoteric local technology'.tokenize(',').collect { it.trim() },
                    places: 'Sealed treaty port, Public ritual not open to outsiders, Outcaste slum home'.tokenize(',').collect { it.trim() }
            ),
            'Zombies'             : new WorldTag(
                    flavor: '''This menace may not take the form of shambling corpses, but some disease, alien artifact, or crazed local practice produces men and women with
habits similar to those of murderous cannibal undead. These outbreaks may be regular elements in local society, either provoked by some malevolent
creators or the consequence of some local condition.''',
                    enemies: 'Soulless maltech biotechnology cult, Sinister governmental agent, Crazed zombie cultist'.tokenize(',').collect { it.trim() },
                    friends: 'Survivor of an outbreak, Doctor searching for a cure, Rebel against the secret malefactors'.tokenize(',').collect { it.trim() },
                    complications: 'The zombies retain human intelligence, The zombies can be cured, The process is voluntary among devotees, The condition is infectious'.tokenize(',').collect { it.trim() },
                    things: 'Cure for the condition, Alien artifact that causes it, Details of the cult’s conversion process'.tokenize(',').collect { it.trim() },
                    places: 'House with boarded-up windows, Dead city, Fortified bunker that was overrun from within'.tokenize(',').collect { it.trim() }
            ),
    ]

    String generateWorldTags() {
        WorldTag accum = new WorldTag()

        List<String> results = []
        for (String tag: pickN(world_tags.keySet(), 2) as List<String>) {
            accum.accumulate(world_tags.get(tag))
            results.add("""\
<h6>${tag}</h6>
<blockquote><em>${world_tags.get(tag).flavor}</em></blockquote>
""".toString())
        }
        results.add("""\
<h6>Enemies</h6>
&nbsp;&nbsp;${pickN(accum.enemies, 4).join('<br/>&nbsp;&nbsp;')}
<h6>Friends</h6>
&nbsp;&nbsp;${pickN(accum.friends, 4).join('<br/>&nbsp;&nbsp;')}
<h6>Complications</h6>
&nbsp;&nbsp;${pickN(accum.complications, 4).join('<br/>&nbsp;&nbsp;')}
<h6>Things</h6>
&nbsp;&nbsp;${pickN(accum.things, 4).join('<br/>&nbsp;&nbsp;')}
<h6>Places</h6>
&nbsp;&nbsp;${pickN(accum.places, 4).join('<br/>&nbsp;&nbsp;')}
""".toString())
        return results.join('')


    }

    String generate() {
        // how many cultures are on the world?
        int numCultures = pick(new RangeMap()
                .with(1..3, 1)
                .with(4..5, 2)
                .with(6, 3)
        ) as int

        List<String> cultures = names.pickCultures(numCultures)
        // pick a name from the 1st culture in list
        String worldName = names.getPlaceName(cultures[0])

        """\
<h3>World</h3>
<h4>Physical Attributes</h4>
<p><strong><small>Atmosphere:</small></strong> ${pick('2d6', atmospheres)}
<br/><strong><small>Temperature:</small></strong> ${pick('2d6', temperatures)}
<br/><strong><small>Biosphere:</small></strong> ${pick('2d6', biospheres)}
</p>
<h4>Cultural Attributes</h4>
<p>
<strong><small>Name:</small></strong> ${worldName}
<br/><strong><small>Cultures:</small></strong> ${cultures.join(', ')}
</p>
<p><strong><small>Population:</small></strong> ${pick('2d6', populations)}
<br/><strong><small>Tech Level:</small></strong> ${pick('2d6', techlevels)}
</p>
<h4>World Tags:</h4>
${generateWorldTags()}
"""
    }
}
