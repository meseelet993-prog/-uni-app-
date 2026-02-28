$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Net.WebSockets
param(
    [Parameter(Mandatory = $true)]
    [string] $Url
)
$ws = New-Object System.Net.WebSockets.ClientWebSocket
try {
    $uri = [Uri] $Url
    $ws.ConnectAsync($uri, [System.Threading.CancellationToken]::None).Wait()
    Write-Output ("WSState=" + $ws.State.ToString())
} catch {
    Write-Output ("WSError=" + $_.Exception.Message)
}
