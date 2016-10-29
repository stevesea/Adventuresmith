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

package org.stevesea.adventuresmith.data.stars_without_number

import groovy.transform.CompileStatic
import org.stevesea.adventuresmith.data.AbstractGenerator

@CompileStatic
class SWNadventure_seed extends AbstractGenerator {
    List<String> seeds = [
'''An enemy seeks to rob a friend of some precious Thing that he has desired for some time.''',
'''A Thing has been discovered on property owned by a Friend, but a Complication risks its destruction.''',
'''A Complication suddenly hits the party while they're out doing some innocuous activity.''',
'''The players unwittingly offend or injure an Enemy, incurring his or her wrath. A Friend offers
help in escaping the consequences.''',
'''Rumor speaks of the discovery of a precious Thing in a distant Place. The players must get to it
before an Enemy does.''',
'''An Enemy has connections with offworld pirates or slavers, and a Friend has been captured by them.''',
'''A Place has been seized by violent revolutionaries or rebels, and a Friend is being held hostage by them.''',
'''A Friend is in love with someone forbidden by social convention, and the two of them need help eloping.''',
'''An Enemy wields tyrannical power over a Friend, relying on the bribery of corrupt local officials
to escape consequences.''',
'''A Friend has been lost in hostile wilderness, and the party must reach a Place to rescue them in
the teeth of a dangerous Complication.''',
'''An Enemy has committed a grave offense against a PC or their family sometime in the past. A Friend
shows the party a weakness in the enemy's defenses.''',
'''The party is suddenly caught in a conflict between two warring families or political parties.''',
'''The party is framed for a crime by an Enemy, and must reach the sanctuary of a Place before they can
regroup and find the Thing that will prove their innocence and their Enemy's perfidy.''',
'''A Friend is threatened by a tragedy of sickness, legal calamity, or public humiliation, and the
only one that seems able to save them is an Enemy.''',
'''A natural disaster or similar Complication strikes a Place while the party is present, causing
great loss of life and property unless the party is able to immediately respond to the injured and trapped.''',
'''A Friend with a young business has struck a cache of pretech, valuable minerals, or precious salvage.
He needs the party to help him reach the Place where the valuables are.''',
'''An oppressed segment of society starts a sudden revolt in the Place the party is
occupying. An Enemy simply lumps the party in with the rebels and tries to put the
revolt down with force. A Friend offers them a way to either help the rebels or
clear their names.''',
'''A vulnerable Friend has been targeted for abduction, and has need of guards. A sudden
Complication makes guarding them from the Enemy seeking their kidnapping much more
difficult. If the Friend is snatched, they must rescue them from a Place.''',
'''A mysterious Place offers the promise of some precious Thing, but access is very
dangerous due to wildlife, hostile locals, or a dangerous environment.''',
'''An Enemy and a Friend both hav elegal claim on a Thing, and seek to undermine each
other's case. The Enemy is willing to do murder if he thinks he can get away with
it.''',
'''An Enemy seeks the death of his brother, a Friend, by arranging the failure of his
grav flyer or shuttlecraft in dangerous terrain while the party is coincidentally
aboard. The party must survive the environment and bring proof of the crime out
with them.''',
'''A Friend seeks to slip word to a lover, one who is also being courted by the Friend's
brother, who is an Enemy. A Complication threatens to cause death or disgrace to
the lover unless they either accept the Enemy's suit or are helped by the party.''',
'''An Enemy is convinced that one of the party has committed adultery with their flirtatious
spouse. He means to lure them to a Place, trap them, and have them killed by the
dangers there.''',
'''An Enemy has been driven insane by exotic recreational drugs or excessive psionic
torching. He fixes on a PC as being his mortal nemesis, and plots elaborate deaths,
attempting to conceal his involvement amid Complications.''',
'''A Friend has stolen a precious Thing from an Enemy and fled into a dangerous, inaccessible
Place. The party must rescue them, and decide what to do with the Thing and the
outraged Enemy.''',
'''An Enemy has realized that their brother or sister has engaged in a socially unacceptable
affair with a Friend, and means to kill both of them unless stopped by the party.''',
'''A Friend has accidentally caused the death of a family member, and wants the party
to help him hide the body or fake an accidental death before his family realizes
what has happened. A Complication suddenly makes the task more difficult.''',
'''A Friend is a follower of a zealous ideologue who plans to make a violent demonstration
of the righteousness of his cause, causing a social Complication. THe Friend will
surely be killed in the aftermath if not rescued or protected by the party.''',
'''A Friend's sibling is to be placed in a dangerous situation they've got no chance
of surviving. The Friend takes their place at the last moment, and will almost certainly
die unless the party aids them.''',
'''Suicide bombers detonate an explosive, chemical, or biological weapon in a Place
occupied by the party where a precious Thing is stored. The PCs must escape before
the Place collapses on top of them, navigating throngs of terrified people in the
process and saving the Thing if possible.''',
'''An Enemy who controls landing permits, oxygen rations, or some other important resource
has a prejudice against one or more of the party members. He demands that they bring
him a Thing from a dangerous Place before he'll give them what they need.''',
'''A Friend in a loveless marriage to an Enemy seeks escape to be with their beloved,
and contacts the party to snatch them from their spouse's guards at a prearranged
place.''',
'''A Friend seeks to elope with their lover, and contacts the party to help them meet
their paramour at a remote, dangerous Place. On arrival, they find that the lover
is secretly an Enemy desirous of their removal and merely lured them to the place
to meet their doom.''',
'''The party receives or finds a Thing which proves the crimes of an Enemy - yet a
Friend was complicit in the crimes, and will be punished as well if the authorities
are involved. And the Enemy will stop at nothing to get the item back.''',
'''A Friend needs to get to a Place on time in order to complete a business contract,
but an Enemy means to delay and hinder them until it's too late, inducing Complications
to the trip.''',
'''A locked pretech stasis pod has been discovered by a Friend, along with directions
to the hidden keycode that will open it. The Place where the keycode is hidden is
now owned by an Enemy.''',
'''A fierce schism has broken out in the local majority religion, and an Enemy is making
a play to take control of the local hierarchy. A Friend is on the side that will
lose badly if the Enemy succeeds, and needs a Thing to prove the error of the Enemy's
faction.''',
'''A former Enemy has been given reason to repent his treatment of a Friend, and secretly
commissions them to help the Friend overcome a Complication. A different Enemy discovers
the connection, and tries to paint the PCs as double agents.''',
'''An alien or human with extremely peculiar spiritual beliefs seeks to visit a Place
for their own reasons. An Enemy of their own kind attempts to stop them before they
can reach the Place, and reveal the Thing that was hidden there long ago.''',
'''A Friend's sibling is an untrained psychic, and has been secretly using his or her
powers to protect the Friend from an Enemy. The neural damage has finally overwhelmed
their sanity, and they've now kidnapped the Friend to keep them safe. The Enemy
is taking this opportunity to make sure the Friend "dies at the hands of their maddened
sibling".''',
'''A Friend who is a skilled precognitive has just received a flash of an impending
atrocity to be committed by an Enemy. He or she needs the party to help them steal
the Thing that will prove the Enemy's plans while dodging the assassins sent to
eliminate the precog.''',
'''A Friend who is an exotic dancer is sought by an Enemy who won't take no for an
answer. The dancer is secretly a Perimeter agent attempting to infiltrate a Place
to destroy maltech research, and plots to use the party to help get him or her into
the facility under the pretext of striking at the Enemy.''',
'''A young woman on an interplanetary tour needs the hire of local bodyguards. She
turns out to be a trained and powerful combat psychic, but touchingly naive about
local dangers, causing a social Complication that threatens to get the whole group
arrested.''',
'''A librarian Friend has discovered an antique databank with the coordinates of a
long-lost pretech cache hidden in a Place sacred to a long-vanished religion. The
librarian is totally unsuited for danger, but necessary to decipher the obscure
religious iconography needed to unlock the cache. The cache is not the anticipated
Thing, but something more dangerous to the finder.''',
'''A fragment of orbital debris clips a shuttle on the way in, and the spaceport is
seriously damaged in the crash. The player's ship or the only vessel capable of
getting them off-planet will be destroyed unless the players can organise a response
to the dangerous chemical fires and radioactives contaminating the port. A Friend
is trapped somewhere in the control tower wreckage.''',
'''A Friend is allied with a reformist religious group that seeks to break the grip
of the current, oppressive hierarchy. The current hierarchs have a great deal of
political support with the authorities, but the commoners resent them bitterly.
The Friend seeks to secure a remote Place as a meeting-place for the theological
rebels.''',
'''A microscopic black hole punctures an orbital station or starship above the world.
Its interaction with the station's artificial grav generators has thrown everything
out of whack, and the station's become a minefield of dangerously high or zero grav
zones. It's tearing itself apart, and it's going to collapse soon. An Enemy seeks
to escape aboard the last lifeboat and to Hell with everyone else. Meanwhile, a
Friend is trying to save his engineer daughter from the radioactive, grav-unstable
engine rooms.''',
'''The planet has a sealed alien ruin, and an Enemy-led cult who worships the vanished
builders. They're convinced that they have the secret to opening and controlling
the power inside the ruins, but they're only half-right. A Friend has found evidence
that shows that they'll only devastate the planet if they meddle with the alien
power planet. The party has to get inside the ruins and shut down the engines before
it's too late. Little do they realize that a few aliens survive inside, in a stasis
field that will be broken by the ruin's opening.''',
'''An Enemy and the group are suddenly trapped in a Place during an accident or Complication.
They must work together to escape before it's too late.''',
'''A telepathic Friend has discovered that an Enemy was responsible for a recent atrocity.
Telepathic evidence is useless on this world, however, and if she's discovered to
have scanned his mind she'll be lobotomized as a 'rogue psychic'. A Thing might
be enough to prove his guilt, if the party can figure out how to get to it without
revealing their Friend's meddling.''',
'''A Friend is responsible for safeguarding a Thing - yet the Thing is suddenly proven
to be a fake. The party must find the real object and the Enemy who stole it or
else their Friend will be punished as the thief.''',
'''A Friend is bitten by a poisonous local animal while in a remote Place. The only
antidote is back at civilization, yet a Complication threatens to delay the group
until it's too late.''',
'''A lethal plague has started among the residents of the town, but a Complication
is keeping aid from reaching them. An Enemy is taking advantage of the panic to
hawk a fake cure at ruinous prices, and a Friend is taken in by him. The Complication
must be overcome before help can reach the town.''',
'''A radical political party has started to institute pogroms against "groups hostile
to the people". A Friend is among those groups, and needs to get out of town before
an Enemy uses the riot as cover to settle old scores.''',
'''An Enemy has sold the party an expensive but worthlessly flawed piece of equipment
before lighting out for the back country. He and his plunder are holed up at a remote
Place.''',
'''A concert of offworld music is being held in town, and a Friend is slated to be
the star performer. Reactionary elements led by an Enemy plot to ruin the "corrupting
noise" with sabotage that risks getting performers killed. Meanwhile, a crowd of
ignorant offworlder fans have landed and are infuriating the locals.''',
'''An Enemy is wanted on a neighboring world for some heinous act, and a Friend turns
up as a bounty hunter ready to bring him in alive. This world refuses to extradite
him, so the capture and retrieval has to evade local law enforcement.''',
'''An unanticipated solar storm blocks communications and grounds the poorly-shielded
grav vehicle that brought the group to this remote Place. Then people start turning
up dead; the storm has awoken a dangerous Enemy beast.''',
'''A Friend has discovered a partially-complete schematic for an ancient pretech refinery
unit that produces vast amounts of something precious on this world - water, oxygen,
edible compounds, or the like. Several remote Places on the planet are indicated
as having the necessary pretech spare parts required to build the device. When finally
assembled, embedded self-modification software in the Thing modifies itself into
a pretech combat bot. The salvage from it remains very valuable.''',
'''A Complication ensnares the party where they are in an annoying but seemingly ordinary
event. In actuality, an Enemy is using it as cover to strike at a Friend or Thing
that happens to be where the PCs are.''',
'''A Friend has a cranky, temperamental artificial heart installed, and the doctor
who put it in is the only one who really understands how it works. The heart has
recently started to stutter, but the doctor has vanished. An Enemy has snatched
him to fit his elite assassins with very unsafe combat mods.''',
'''A local clinic is doing wonders providing free health care to the poor. In truth,
it's a front for an offworld eugenics cult, with useful "specimens" kidnapped and
shipped offworld while 'cremated remains' are given to the family. A Friend is snatched
by them, but the party knows they'd have never consented to cremation as the clinic
staff claim.''',
'''Space pirates have cut a deal with an isolated backwoods settlement, off loading
their plunder to merchants who meet them there. A Friend goes home to family after
a long absence, but is kidnapped or killed before they can bring back word of the
dealings. Meanwhile, the party is entrusted with a valuable Thing that must be brought
to the Friend quickly.''',
'''A reclusive psychiatrist is offering treatment for violent mentally ill patients
at a remote Place. His treatment seem to work, calming the subjects and returning
them to rationality, though major memory loss is involved and some severe social
clumsiness ensues. In actuality, he's removed large portions of their brains to
fit them with remote-control units slaved to an AI in his laboratory. He intends
to use them as drones to acquire more "subjects", and eventual control of the town.''',
'''Vital medical supplies against an impending plague have been shipped in from offworld,
but the spike drive craft that was due to deliver them misjumped, and has arrived
in-system as a lifeless wreck transmitting a blind distress signal. Whoever gets
there first can hold the whole planet hostage, and an Enemy means to do just that.''',
'''A Friend has spent a substatial portion of their wealth on an ultra-modern new domicile,
and invites the party to enjoy a weekend there. An Enemy has hacked the house's
computer system to trap the inhabitants inside and use the automated fittings to
kill them.''',
'''A mud slide, hurricane, earthquake, or other form of disaster strikes a remote settlement.
The party is the closest group of responders, and must rescue the locals while dealing
with the unearthed, malfunctioning pretech Thing that threatens to cause an even
greater calamity if not safely defused.''',
'''A Friend has found a lost pretech installation, and needs help to loot it. By planetary
law, the contents belong to the government.''',
'''An Enemy mistakes the party for the kind of offworlders who will murder innocents
for pay - assuming they aren't that kind, at least. He's sloppy with the contact
and unwittingly identifies himself, letting the players know that a Friend will
shortly die unless the Enemy is stopped.''',
'''A party member is identified as a prophesied savior for an oppressed faith or ethnicity.
The believers obstinately refuse to believe any protestations to the contrary, and
a cynical Enemy in government decides the PC must die simply to prevent the risk
of uprising. An equally cynical Friend is determined to push the PC forward as a
savior, because that's what's needed.''',
'''Alien beasts escape from a zoo and run wild through the spectators. The panicked
owner offers large rewards for recapturing them live, but some of the beasts are
quite dangerous.''',
'''A trained psychic is accused of going feral by an Enemy. The psychic had already
suffered severe neural damage before being found for training, so brain scans cannot
establish physical signs of madness. The psychic seems unstable, but not violent
- at least, on short acquaintance. The psychic offers a psychic PC the secrets of
psychic mentorship training if they help him flee.''',
'''A Thing is the token of rulership on this world, and it's gone missing. If it's
not found rapidly, the existing ruler will be deposed. Evidence left at a Place
suggests that an enemy has it, but extralegal means are necessary to investigate
fully.''',
'''Psychics are vanishing, including a Friend. They're being kidnapped by an ostensibly-rogue
government researcher who is using them to research the lost psychic disciplines
that helped enable pretech manufacturing, and being held at a remote Place. The
snatcher is a small-time local Enemy with unnaturally ample resources.''',
'''A Friend desperately seeks to hide evidence of some past crime that will ruin his
life should it come to light. An Enemy holds the Thing that proves his involvement,
and blackmails him ruthlessly.''',
'''A courier mistakes the party for the wrong set of offworlders, and wordlessly deposits
a Thing with them that implies something awful - med-frozen, child-sized human organs,
for example, or a private catalog of gengineered human slaves. The courier's boss
shortly realizes his error, and this Enemy tries to silence the PCs while preserving
the Place where his evil is enacted.''',
'''A slowboat system freighter is taken over by Enemy separatist terrorists at the
same time as the planet's space defenses are taken offline by internal terrorist
attacks. The freighter is aimed straight at the starport, and will crash into it
in hours if not stopped.''',
'''Alien artifacts on the planet's surface start beaming signals into the system's
asteroid belt. The signals provoke a social Complication in panicked response, and
an Enemy seeks to use the confusion to take over. The actual effect of the signals
might be harmless, or might summon a long-lost alien AI warship to scourge life
from the world.''',
'''An alien ambassador Friend is targeted by xenophobe Enemy assassins. Relations are
so fragile that if the ambassador even realizes that humans are making a serious
effort to kill him, the result may be war.''',
'''A new religion is being preached by a Friend on this planet. Existing faiths are
not amused, and an Enemy among the hierarchy is provoking the people to persecute
the new believers, hoping for things to get out of hand.''',
'''An Enemy was once the patron of a Friend until the latter was betrayed. Now the
Friend wants revenge, and they think they have the information necessary to get
past the Enemy's defenses.''',
'''Vital life support or medical equipment has been sabotaged by offworlders or zealots,
and must be repaired before time runs out. The only possible source of parts is
at a Place, and the saboteurs can be expected to be working hard to get there and
destroy them, too.''',
'''A Friend is importing offworld tech that threatens to completely replace the offerings
of an Enemy businessman. The Enemy seeks to sabotage the friend's stock, and thus
'prove' its inferiority.''',
'''An Exchange diplomat is negotiating for the opening of a branch of the interstellar
bank on this world. An Enemy among the local banks wants to show the world as being
ungovernably unstable, so provokes Complications and riots around the diplomat.''',
'''An Enemy is infuriated by the uppity presumption of an ambitious Friend of a lower
social caste, and tries to pin a local Complication on the results of his unnatural
rejections of his proper place.''',
'''A Friend is working for an offworld corporation to open a manufactory, and is ignoring
local traditions that privilege certain social or ethnic groups, giving jobs to
the most qualified workers instead. An angry Enemy seeks to sabotage the factory.''',
'''An offworld musician who was revered as little less than a god on his homeworld
requires bodyguards. He immediately acquires Enemies on this world with his riotous
ways, and his guards must keep him from getting arrested if they are to be paid.''',
'''Atmospheric disturbances, dust storms, or other particulate clouds suddenly blow
into town, leaving the settlement blind. An Enemy commits a murder during the darkness,
and attempts to frame the players as convenient scapegoats.''',
'''An Enemy spikes the oxygen supply of an orbital station or unbreathable-atmosphere
hab dome with hallucinogens as cover for a theft. Most victims are merely confused
and disoriented, but some become violent in their delusions. By chance, the party's
air supply was not contaminated.''',
'''By coincidence, one of the party members is wearing clothing indicative of membership
in a violent political group, and thus the party is treated in friendly fashion by a local
Enemy for no obvious reason. The Enemy assumes that the party will go along with some vicious
crime without complaint, and the group isn't informed of what's in the offing until they're in deep.''',
'''A local ruler wishes outworlders to advise him of the quality of his execrable poetry -
and is the sort to react very poorly to anything less than evidently sincere and fulsome
praise. Failure to amuse the ruler results in the party being dumped in a dangerous Place
to "experience truly poetic solitude".''',
'''A Friend among the locals is unreasonably convinced that offworlder tech can repair anything,
and has blithely promised a powerful local Enemy that the party can easily fix a damaged pretech
Thing. The Enemy has invested in many expensive spare parts, but the truly necessary pieces are
kept in a still-dangerous pretech installation  in a remote Place.''',
'''The party's offworld comm gear picks up a chance transmission from the local government and
automatically descrambles the primitive encryption key. The document is proof that an Enemy in
government intends to commit an atrocity against a local village with a group of "deniable"
renegades in order to steal a valuable Thing kept in the village.''',
'''A Friend belongs to a persecuted faith, ethnicity, or social class, and appeals for the PCs
to help a cell of rebels get offworld before the Enemy law enforcement finds them.''',
'''A part on the party's ship or the only other transport out has failed, and needs immediate
replacement. The only available part is held by an Enemy, who will only willingly relinquish it
in exchange for a Thing held by an innocent Friend who will refuse to sell at any price.''',
'''Eugenics cultists are making gengineered slaves out of genetic material gathered at a local
brothel. Some of the unnaturally tempting slaves are being slipped among the prostitutes as bait
to infatuate powerful officials, while others are being sold under the table to less scrupulous
elites.''',
'''Evidence has been unearthed at a Place that substantial portions of the planet are actually
owned by members of an oppressed and persecuted group. The local courts have no intention of
recognizing the rights, but the codes with the ownership evidence would allow someone to completely
bypass a number of antique pretech defenses around the planetary governor's palace. A Friend wants
the codes to pass to his friends among the group's rebels.''',
'''A crop smut threatens the planet's agriculture, promising large-scale famine. A Friend finds
evidence that a secret government research station in the system's asteroid belt was conducting
experiments in disease-resistant crop strains for the planet before the Silence struck and cut
off communication with the station. The existing government considers it a wild goose chase, but
the party might choose to help. The station has stasis-frozen samples of the crop sufficient to
avert the famine, but it also has less pleasant relics...''',
'''A grasping Enemy in local government seizes the party's ship for some trifling offense. The
Enemy wants to end offworld trade, and is trying to scare other traders away. The starship is
held within a military cordon, and the Enemy is confident that by the time other elements of the
government countermand the order, the free traders will have been spooked off.''',
'''A seemingly useless trinket purchased by a PC turns out to be the security key to a lost pretech
facility. It was sold by accident by a bungling and now-dead minion of a local Enemy, who is hot
after the party to "reclaim" his property... preferably after the party defeats whatever automatic
defenses and bots the facility might still support.'''
    ]


    String generate () {
        pick(seeds)
    }
}
