private void buildLinkToLiverpool(BrassComputerPlayerAction bcpa, int player_id)
{	
ArrayList<Integer> goodLinks = new ArrayList<Integer>();
fillGoodLinks(goodLinks);
Iterator iter = goodLinks.iterator();
while(iter.hasNext())
{
int linkID = (int)iter.next();
if(brass_game.canBuildLink(linkID, player_id))
{
bcpa.selectLinkAction(getCardForNonBuildAction(player_id), linkID);
return;
}
}
int num_connections = brass_game.getNumLinks();
for (int i = 1; i <= num_connections; i++)
{
boolean can_build_link = brass_game.canBuildLink(i, player_id);
if (can_build_link)
{
bcpa.selectLinkAction(getCardForNonBuildAction(player_id), i);
System.out.println("Regular Link");
return;
}
}
}